
document.addEventListener('DOMContentLoaded', () => {

    const filterSelect = document.getElementById('filter');
    const searchInput = document.getElementById('searchValue');
    const searchButton = document.getElementById('searchButton');

    if (!filterSelect || !searchInput || !searchButton) return;

    function toggleSearchButton() {
      const filter = filterSelect.value;
      const value = searchInput.value.trim();
      searchButton.disabled = !filter || !value;
    }

    toggleSearchButton();

    filterSelect.addEventListener('change', toggleSearchButton);
    searchInput.addEventListener('input', toggleSearchButton);

});