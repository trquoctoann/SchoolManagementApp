const darkToggle = document.querySelector('#dark-toggle');
const html = document.querySelector('html');

let isDark = localStorage.theme === 'dark' || (!('theme' in localStorage) && window.matchMedia('(prefers-color-scheme: dark)').matches);

if (isDark) {
  html.classList.add('dark');
} else {
  html.classList.remove('dark');
}

darkToggle.addEventListener('click', function () {
  if (isDark) {
    html.classList.remove('dark');
    localStorage.theme = 'light';
  } else {
    html.classList.add('dark');
    localStorage.theme = 'dark';
  }

  isDark = !isDark;
});

document.addEventListener('DOMContentLoaded', function () {
  const toggleButton = document.querySelector('[data-collapse-toggle="navbar-sticky"]');
  const menu = document.getElementById('navbar-sticky');

  toggleButton.addEventListener('click', function () {
    menu.classList.toggle('hidden');
  });
  document.addEventListener('click', function (event) {
    if (!menu.contains(event.target) && !toggleButton.contains(event.target) && !menu.classList.contains('hidden')) {
      menu.classList.add('hidden');
    }
  });
});

document.getElementById('dropdownAccountLink').addEventListener('click', function (event) {
  const dropdownAccount = document.getElementById(this.getAttribute('data-dropdown-toggle'));
  if (dropdownAccount.classList.contains('hidden')) {
    dropdownAccount.classList.remove('hidden');
  } else {
    dropdownAccount.classList.add('hidden');
  }
});

document.addEventListener('click', function (event) {
  if (!event.target.closest('#dropdownAccountLink') && !event.target.closest('#dropdownAccount')) {
    document.getElementById('dropdownAccount').classList.add('hidden');
  }
});

document.getElementById('dropdownEducationLink').addEventListener('click', function (event) {
  const dropdownEducation = document.getElementById(this.getAttribute('data-dropdown-toggle'));
  if (dropdownEducation.classList.contains('hidden')) {
    dropdownEducation.classList.remove('hidden');
  } else {
    dropdownEducation.classList.add('hidden');
  }
});

document.addEventListener('click', function (event) {
  if (!event.target.closest('#dropdownEducationLink') && !event.target.closest('#dropdownEducation')) {
    document.getElementById('dropdownEducation').classList.add('hidden');
  }
});
