let isShowing = false;
let activeSection = null;

/**
 * Decides wether to load or hide the section.
 */
function toggleSection(section){
  if(isShowing) (isShowing =  hideSection());
  if(activeSection === section) (activeSection = null);
  else{
    isShowing = loadSection(section);
    activeSection = section;
  }
};

/**
 * Loads the specified section.
 */
function loadSection(section){
  $("#option-result").load(`${section}.html`); 
  return true;
};

/**
 * Hides the activity section.
 */
function hideSection(){
  $("#option-result").empty();
  return false;
};
