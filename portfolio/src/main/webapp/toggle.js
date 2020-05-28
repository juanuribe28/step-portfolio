let isShowing = false;
let activeSection = null;

function toggleSection(section){
  if(isShowing) (isShowing =  hideSection());
  if(activeSection === section) (activeSection = null);
  else{
    isShowing = loadSection(section);
    activeSection = section;
  }
};

function loadSection(section){
  $("#option-result").load(`${section}.html`); 
  return true;
};

function hideSection(){
  $("#option-result").empty();
  return false;
};
