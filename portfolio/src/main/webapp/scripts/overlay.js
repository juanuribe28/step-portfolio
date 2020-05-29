let isShowingOverlay = false;
let activeOverlay = null;

/**
 * Decides wether to show or hide the overlay.
 */
function toggleOverlay(section){
  if(isShowingOverlay) (isShowingOverlay =  hideOverlay(activeOverlay));
  if(activeOverlay === section) (activeOverlay = null);
  else{
    isShowingOverlay = showOverlay(section);
    activeOverlay = section;
  }
};

/**
 * Shows the overlay of the specified section.
 */
function showOverlay(section){
  let overlay = document.querySelector(`#${section} .overlay`);
  overlay.style.opacity = "1";
  return true;
};

/**
 * Hides the overlay of the specified section.
 */
function hideOverlay(section){
  let overlay = document.querySelector(`#${section} .overlay`);
  overlay.style.opacity = "0";
  return false;
};