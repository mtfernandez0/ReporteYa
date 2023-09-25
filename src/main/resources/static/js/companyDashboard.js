const popup = document.getElementById("popup");
const POPUP_NAME = document.getElementById("popup-company-name");
const POPUP_EMAIL = document.getElementById("popup-company-email");
const POPUP_WEBSITE = document.getElementById("popup-company-website");
const POPUP_LOCATION = document.getElementById("popup-company-location");
const POPUP_DESCRIPTION = document.getElementById("popup-company-description");
const POPUP_IMG = document.getElementById("popup-company-img");

let isOpen = false;

function showPopup(index, name, description, website, location, imgName, email) {
    if (isOpen) return;
    popup.style.display = "block";
    updatePopupPosition();

    POPUP_NAME.innerText = name;
    POPUP_DESCRIPTION.innerText = description;
    POPUP_WEBSITE.style.display = website === null ? "none" : "block";
    POPUP_WEBSITE.innerText = website;
    POPUP_LOCATION.innerText = location;
    getImage(imgName);
    POPUP_EMAIL.innerText = email;

    window.setTimeout(() => {
        popup.style.opacity = 1;
        popup.style.transform = 'scale(1)';
    }, 0);
    isOpen = true;
}

function closePopup() {
    if (!isOpen) return;
    isOpen = false;
    popup.style.opacity = 0;
    popup.style.transform = 'scale(0)';
    window.setTimeout(function () {
        popup.style.display = 'none';
    }, 300);
}

function getImage(imageName){

    fetch(`/images/${imageName}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            const contentType = response.headers.get('content-type');
            return response.blob()
                .then(blob => {
                    const objectURL = URL.createObjectURL(blob);
                    return { contentType, objectURL };
                });
        })
        .then(data => {
            POPUP_IMG.src = data.objectURL;
        })
        .catch(error => {
            console.error('Fetch error:', error);
        });
}

/**
 * Update top for popup-container
 */
function updatePopupPosition() {

    const popupContainer = document.getElementById("popup-container");

    if (window.getComputedStyle(popup, null).display === 'block') {

        const topPositionPixels = parseFloat(window.getComputedStyle(popupContainer).top);
        const viewportHeight = window.innerHeight;
        const topPercentage = ((topPositionPixels / viewportHeight) * 100) + 375;
        popupContainer.style.top = topPercentage + 'px';
    }
}

window.addEventListener('resize', updatePopupPosition);