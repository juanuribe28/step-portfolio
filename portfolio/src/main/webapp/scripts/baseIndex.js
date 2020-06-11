$("document").ready(function() {
  let headerTemplatePromise = loadTemplate('content/header.html');
  let loginObjPromise = fetch('/auth').then(promiseResponse => promiseResponse.json());
  Promise.all([headerTemplatePromise, loginObjPromise]).then((values) => {
    let headerTemplate = values[0];
    let loginStatus = values[1];
    let htmlHeader = Mustache.render(headerTemplate, loginStatus);
    $("header").html(htmlHeader);
  });
  $("footer").load('content/footer.html').addClass('center');
})

/**
 * Loads the template from the url.
 * Returns a promise of the template.
 */
function loadTemplate(url) {
  const templatePromise = fetch(url).then(promiseResponse => promiseResponse.text());
  return templatePromise;
}
