$("document").ready(function() {
  let headerTemplatePromise = loadTemplate('content/header.html');
  let formTemplatePromise = loadTemplate('content/commentForm.html');
  let loginObjPromise = loadObject('/auth');
  Promise.all([headerTemplatePromise, formTemplatePromise, loginObjPromise]).then((values) => {
    let headerTemplate = values[0];
    let formTemplate = values[1];
    let loginStatus = values[2];
    let htmlHeader = Mustache.render(headerTemplate, loginStatus);
    let htmlForm = Mustache.render(formTemplate, loginStatus);
    $("header").html(htmlHeader);
    $("div#form").html(htmlForm);
  });
  $("footer").load('content/footer.html').addClass('center');
})


