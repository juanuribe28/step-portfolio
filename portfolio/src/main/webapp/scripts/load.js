/**
 * Loads the template from the url.
 * Returns a promise of the template.
 */
function loadTemplate(url) {
  const templatePromise = fetch(url).then(promiseResponse => promiseResponse.text());
  return templatePromise;
}

/**
 * Loads the object from the url.
 * Returns a promise of the object.
 */
function loadObject(url) {
  const templatePromise = fetch(url).then(promiseResponse => promiseResponse.json());
  return templatePromise;
}