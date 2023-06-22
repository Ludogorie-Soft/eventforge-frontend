var searchInput = document.getElementById('search');

function myFunction() {
    var x = document.getElementById("mynavbar");
    if (x.className === "navbar") {
      x.className += " responsive";
    } else {
      x.className = "navbar";
    }
  }

// Add an event listener to the search input
searchInput.addEventListener('keydown', ()=> {
        searchFunction();
    }
);

function searchFunction() {
  // Extract the search query
  var searchQuery = searchInput.value.toLowerCase();

  // Get all the SearchTitle elements
  var searchTitles = document.querySelectorAll('#SearchTitle');

  // Loop through the search titles and show or hide the cards based on the search query
  for (var i = 0; i < searchTitles.length; i++) {
      var searchTitle = searchTitles[i].textContent.toLowerCase();
      var cardContainer = searchTitles[i].closest('.myCard');

      if (searchTitle.includes(searchQuery)) {
          cardContainer.style.display = 'block';  // Show the card
      } else {
          cardContainer.style.display = 'none';   // Hide the card
      }
  }
}


function showPopup(button) {
  var popup = document.getElementById("popup");
  var notVisibleContent = button.parentNode.querySelector(".notVisible");

  // Append the notVisible content to the existing content in the popup
  popup.innerHTML = `

    <button id="popupCloseButton"class="popupCloseButton" onclick="hidePopup()"><i class="fa fa-close"></i></button>
    ${notVisibleContent.innerHTML}
  `

  // Show the popup with animation
  popup.classList.add("show");
}

// Function to hide the popup
function hidePopup() {
  var popup = document.getElementById("popup");

  // Hide the popup
  popup.classList.remove("show");
}

// Get all buttons with class "cta"
var ctaButtons = document.querySelectorAll(".cta");

// Add click event listener to each button
ctaButtons.forEach(function(button) {
  button.addEventListener("click", function(event) {
    showPopup(this);
  });
});

