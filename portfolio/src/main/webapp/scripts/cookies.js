/**
 * Create a new cookie based on name, value and days before expiring.
 */
function setCookie(name, value, expirationDays) {
  let date = new Date();
  date.setTime(date.getTime+(expirationDays*24*60*60*1000));
  document.cookie = `${name}=${value};expires=${expirationDays}`;
}

/**
 * Set the cookie value of a change event.
 */
function setChangeCookie(event) {
  let name = event.currentTarget.id;
  let value = event.currentTarget.value;
  setCookie(name, value, 1);
}

/**
 * Get the value of a cookie.
 */
function getCookie(name) {
  let decodedCookie = decodeURIComponent(document.cookie);
  let cookies = decodedCookie.split(';');
  for (let i = 0; i < cookies.length; i++) {
    cookieParts = cookies[i].trim().split("=");
    if (cookieParts[0] === name) {
      return cookieParts[1];
    }
  }
  return "";
}

/**
 * Load the cookie value of a select element to the DOM.
 */
function loadSelectValueCookie(cookieName) {
  cookieValue = getCookie(cookieName);
  if (cookieValue !== "") {
      document.getElementById(`${cookieValue}-option`).selected = "selected";
  }
}

/**
 * Load the cookie value of a numeric input element to the DOM.
 */
function loadNumericValueCookie(cookieName) {
  cookieValue = getCookie(cookieName);
  if (cookieValue !== "") {
      document.getElementById(cookieName).value = cookieValue;
  }
}
