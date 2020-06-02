let isShowingOverlay = false;
let activeOverlay = null;

/**
 * Decides wether to show or hide the overlay.
 */
function toggleOverlay(section) {
  if (isShowingOverlay) {
    hideOverlay(activeOverlay);
  }
  
  if (activeOverlay === section) {
    activeOverlay = null;
  } else {
    showOverlay(section);
    activeOverlay = section;
  }
}

/**
 * Shows the overlay of the specified section.
 */
function showOverlay(section) {
  changeOverlayOpacity(section, "1");
  isShowingOverlay = true;
  return;
}

/**
 * Hides the overlay of the specified section.
 */
function hideOverlay(section) {
  changeOverlayOpacity(section, "0");
  isShowingOverlay = false;
  return;
}

/**
 * Cahnges the opacity of the overlay.
 */
function changeOverlayOpacity(section, opacity) {
  let overlay = document.querySelector(`#${section} .overlay`);
  overlay.style.opacity = opacity;
  return;
}
