const options = document.querySelectorAll(".menu-option");
const factButton = document.querySelector("#fact-button");

for(let i=0; i<options.length; i++){
  options[i].addEventListener('click', function(){toggleOverlay(options[i].id)})
  options[i].addEventListener('click', function(){toggleSection(options[i].id)})
};

factButton.addEventListener('click', addRandomFact);