let isShowingSection = false;
let activeSection = null;

/**
 * Decides wether to load or hide the section.
 */
function toggleSection(section) {
  if (isShowingSection) {
    hideSection();
  }
  if (activeSection === section) {
    activeSection = null;
  } else {
    loadSection(section);
    activeSection = section;
  }
}

/**
 * Loads the specified section.
 */
function loadSection(section) {
  $("#option-result").load(`${section}.html`);
  isShowingSection = true; 
  return;
}

/**
 * Hides the activity section.
 */
function hideSection() {
  $("#option-result").empty();
  isShowingSection = false;
  return;
}
