document.addEventListener('DOMContentLoaded', function () {

    if (window.location.pathname === '/weather') {
        showForecast(0);
    }

    locationSuggestions();

});

/*
 * Updates the weather forecast display for a specific day.
 * Highlights the selected day tab and fetches the corresponding forecast HTML from the server.
 *
 * @param {number} dayOffset - Index of the day to show (0 = today, 1 = tomorrow, 2 = day after tomorrow)
 */
function showForecast(dayOffset) {

    // Toggle 'active' class on day navigation buttons to highlight the selected day
    document.querySelectorAll('.weather .nav-link')
            .forEach((btn, index) =>
                btn.classList.toggle('active', index === dayOffset)
            );

    // Fetch the forecast HTML for the selected day from the server and insert it into the weather_container
    fetch('/weather/' + dayOffset)
            .then(response => response.text())
            .then(html => {
                document.getElementById('weather_container').innerHTML = html;
            });
};


/*
 * Enables autocomplete suggestions for the location input form field.
 * Fetches location suggestions from the backend API and updates a dropdown list.
 */
function locationSuggestions() {
    const addressInput = document.getElementById('address');
    const suggestionList = document.getElementById('location_suggestions');
    const latInput = document.getElementById('latitude');
    const lonInput = document.getElementById('longitude');

    if (!addressInput) return;

    addressInput.addEventListener('input', async () => {
        const query = addressInput.value;

        // If input is empty, clear suggestions (avoids errors if user deletes his input again)
        if (!query) {
            suggestionList.innerHTML = '';
            return;
        }

        // Fetch location suggestions from the backend API
        const response = await fetch(`/geocode?query=${encodeURIComponent(query)}`);
        const suggestions = await response.json();

        // Clear previous suggestions
        suggestionList.innerHTML = '';

        // Add each suggestion as a clickable list item
        suggestions.forEach(s => {
            const li = document.createElement('li');
            li.textContent = s.address; // display the human-readable address

            // On click, populate input fields with selected location
            li.addEventListener('click', () => {
                addressInput.value = s.address;
                latInput.value = s.latitude;
                lonInput.value = s.longitude;

                // Clear suggestions after selection
                suggestionList.innerHTML = '';
            });
            suggestionList.appendChild(li);
        });
    });
}