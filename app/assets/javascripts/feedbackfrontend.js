/* global GOVUK */
/* eslint-disable func-names, no-var, prefer-arrow-callback */

/**
 * NOTE: No arrow functions [`()=>{}`] because sbt-uglify doesn't like ES6+ code.
 */

var focusedEl;
var scrollPos;
var mediaQueryList;
var html = document.documentElement;
var htmlTouchEvents = html.classList.contains('touchevents');
var backLink = document.querySelector('#back-link');
var inputTypes = document.querySelectorAll(
  '[data-type="currency"] > input[type="text"], [data-type="percentage"] > input[type="text"]'
);
var inputTypeNumber = document.querySelector('input[type=number]');
var detailsNotOpen = document.querySelectorAll('details:not([open])');
var detailsPrintOpen = document.querySelectorAll('details.print--open');
var errorSummary = document.querySelector('.error-summary');
var noDetails = document.querySelector('.no-details');
var classHeadingMedium = 'heading-medium';
var classPrintOpen = 'print--open';

/**
 * @description Replacement for jQuery’s `$(document).ready(function()`
 * @example ready(function () {…}
 * @function ready
 */
var ready = function (callback) {
  if (document.readyState !== 'loading') callback();
  else document.addEventListener('DOMContentLoaded', callback);
};

/**
 * @description Replacement for jQuery’s `$('.govuk-cat__larry').length` check.
 * @example isInPage(document.querySelector('.govuk-cat__larry'))
 * @function isInPage
 * @param {HTMLElement} element - The element to check
 * @returns {boolean} - Whether the element is in the page or not
 */
function isInPage(node) {
  return node === document.body ? false : document.body.contains(node);
}

/**
 * Handle number inputs
 */
function numberInputs() {
  /**
   * Set currency fields to number inputs on touch devices
   * this ensures on-screen keyboards display the correct style
   * don't do this for FF as it has issues with trailing zeroes
   */
  if (
    htmlTouchEvents &&
    window.navigator.userAgent.indexOf('Firefox') === -1
  ) {
    inputTypes.forEach(function (input) {
      input.setAttribute('type', 'number');
      input.setAttribute('step', 'any');
      input.setAttribute('min', '0');
    });
  }

  if (isInPage(inputTypeNumber)) {
    /**
     * Disable mouse wheel and arrow keys (38,40) for number inputs
     * to prevent mis-entry also disable commas (188) as they will
     * silently invalidate entry on Safari 10.0.3 and IE11
     */
    document
      .querySelector('form')
      .addEventListener(inputTypeNumber, function (e) {
        this.addEventListener('wheel', function () {
          e.preventDefault();
        });
      });

    document
      .querySelector('form')
      .addEventListener(inputTypeNumber, function () {
        this.removeEventListener('wheel');
      });

    document
      .querySelector('form')
      .addEventListener(inputTypeNumber, function (e) {
        this.addEventListener('keydown', function () {
          if (e.which === 38 || e.which === 40 || e.which === 188)
            e.preventDefault();
        });
      });
  }
}

function beforePrintCall() {
  detailsPrintOpen.querySelector('summary').classList.add(classHeadingMedium);

  if (isInPage(noDetails)) {
    /**
     * 1. focusedEl: element with current focus, give it back later.
     * 2. scrollPos: current scroll position.
     */
    focusedEl = document.activeElement;
    scrollPos = window.pageYOffset;

    detailsNotOpen.forEach(function (el) {
      el.classList.add(classPrintOpen);
      el.querySelector('summary').dispatchEvent(new Event('click'));
    });

    /**
     * 1. blur focus from current element in case original cannot regain focus
     * 2. return focus to original element if possible
     * 3. return to scroll position
     */
    document.activeElement.addEventListener('blur');
    focusedEl.addEventListener('focus');
    window.scrollTo(0, scrollPos);
  } else {
    detailsPrintOpen
      .addAttribute('open', 'open')
      .classList.add(classPrintOpen);
  }
}

function afterPrintCall() {
  detailsPrintOpen
    .querySelector('summary')
    .classList.remove(classHeadingMedium);

  if (isInPage(noDetails)) {
    /**
     * 1. focusedEl: element with current focus, give it back later.
     * 2. scrollPos: current scroll position.
     */
    focusedEl = document.activeElement;
    scrollPos = window.pageYOffset;

    detailsPrintOpen.forEach(function (el) {
      el.classList.remove(classPrintOpen);
      el.querySelector('summary').dispatchEvent(new Event('click'));
    });

    /**
     * 1. blur focus from current element in case original cannot regain focus
     * 2. return focus to original element if possible
     * 3. return to scroll position
     */
    document.activeElement.addEventListener('blur');
    focusedEl.addEventListener('focus');
    window.scrollTo(0, scrollPos);
  } else {
    detailsPrintOpen.removeAttribute('open').classList.remove(classPrintOpen);
  }
}

ready(function () {
  /**
   * Initialise show-hide-content
   * Toggles additional content based on radio/checkbox input state
   */
  var showHideContent = new GOVUK.ShowHideContent();
  showHideContent.init();

  /**
   * prevent resubmit warning
   */
  if (
    window.history &&
    window.history.replaceState &&
    typeof window.history.replaceState === 'function'
  ) {
    window.history.replaceState(null, null, window.location.href);
  }

  if (backLink) {
    backLink.addEventListener('click', function (e) {
      e.preventDefault();
      e.stopPropagation();
      window.history.back();
    });
  }

  numberInputs();

  /**
   * Move immediate focus to any error summary
   */
  if (isInPage(errorSummary)) {
    errorSummary.addEventListener('focus');
  }

  /**
   * print call functions for Chrome
   */
  if (typeof window.matchMedia !== 'undefined') {
    mediaQueryList = window.matchMedia('print');
    mediaQueryList.addListener(function (mql) {
      if (mql.matches) {
        beforePrintCall();
      }
      if (!mql.matches) {
        afterPrintCall();
      }
    });
  }

  /**
   * print call functions for Firefox and IE (above does not work)
   */
  window.onbeforeprint = function () {
    beforePrintCall();
  };

  window.onafterprint = function () {
    afterPrintCall();
  };
});
