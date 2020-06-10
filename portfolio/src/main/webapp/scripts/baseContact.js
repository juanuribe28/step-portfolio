$("document").ready(function() {
  let headerTemplatePromise = loadTemplate('content/header.html');
  let formTemplatePromise = loadTemplate('content/commentForm.html');
  let loginObjPromise = loadObject('/auth');
  let blobstoreUrlPromise = loadTemplate('/blobstore-upload-url')
  Promise.all([headerTemplatePromise, formTemplatePromise, loginObjPromise, blobstoreUrlPromise])
  .then((values) => {
    let headerTemplate = values[0];
    let formTemplate = values[1];
    let loginStatus = values[2];
    let blobstoreUrl = values[3];
    let htmlHeader = Mustache.render(headerTemplate, loginStatus);
    loginStatus.blobstoreUrl = blobstoreUrl;
    let htmlForm = Mustache.render(formTemplate, loginStatus);
    $("header").html(htmlHeader);
    $("div#form").html(htmlForm);
  });
  $("footer").load('content/footer.html').addClass('center');
})
