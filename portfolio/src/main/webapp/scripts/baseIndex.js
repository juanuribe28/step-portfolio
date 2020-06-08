$("document").ready(function() {
  let templatePromise = loadTemplate('content/header.html');
  let objPromise = loadObject('/auth');
  Promise.all([templatePromise, objPromise]).then((values) => {
    let template = values[0];
    let obj = values[1];
    let html = Mustache.render(template, obj);
    $("header").html(html);
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
