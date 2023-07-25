 var popup = document.querySelector(".popup");
  var result = popup.dataset.result;

  if (result && result.trim() !== '') {
    var popupElement = document.createElement("div");
    popupElement.innerHTML = result;
    popupElement.style.position = "fixed";
    popupElement.style.top = "50%";
    popupElement.style.left = "50%";
    popupElement.style.transform = "translate(-50%, -50%)";
    popupElement.style.backgroundColor = "lightgray";
    popupElement.style.padding = "20px";
    popupElement.style.border = "1px solid gray";
    popupElement.style.borderRadius = "5px";
    popupElement.style.opacity = "1";
    popupElement.style.transition = "opacity 2s";

    var closeButton = document.createElement("button");
    closeButton.innerHTML = "X";
    closeButton.style.position = "absolute";
    closeButton.style.top = "5px";
    closeButton.style.right = "5px";
    closeButton.style.border = "none";
    closeButton.style.backgroundColor = "red";
    closeButton.style.color = "white";
    closeButton.style.fontSize = "12px";
    closeButton.style.width = "20px";
    closeButton.style.height = "20px";
    closeButton.style.borderRadius = "50%";
    closeButton.style.cursor = "pointer";

    popupElement.appendChild(closeButton);
    document.body.appendChild(popupElement);

    function closePopup() {
      popupElement.style.opacity = "0";
      setTimeout(function() {
        document.body.removeChild(popupElement);
      }, 2000);
    }

    closeButton.addEventListener("click", closePopup);
    setTimeout(closePopup, 5000);
  }