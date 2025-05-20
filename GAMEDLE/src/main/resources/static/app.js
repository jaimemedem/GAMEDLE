document.addEventListener('DOMContentLoaded', () => {
  setupNav();
  loadSection(location.hash || '#inicio');
});

window.addEventListener('hashchange', () => {
  loadSection(location.hash);
});

function setupNav() {
  // Logout
  document.getElementById('btn-logout')
    .addEventListener('click', () => {
      fetch('/api/users/me/session', { method: 'DELETE' })
        .then(() => window.location = 'login.html');
    });

  // Unsubscribe
  document.getElementById('btn-unsubscribe')
    .addEventListener('click', () => {
      if (confirm('¿Seguro que quieres darte de baja?')) {
        fetch('/api/users/me', { method: 'DELETE' })
          .then(() => window.location = 'login.html');
      }
    });

  // Navegación
  ['link-inicio', 'link-wordle'].forEach(id => {
    const el = document.getElementById(id);
    el.addEventListener('click', e => {
      e.preventDefault();
      location.hash = el.getAttribute('href');
    });
  });
}

function fetchProfile() {
  return fetch('/api/users/me')
    .then(res => res.json());
}

function showProfile() {
  return fetchProfile().then(profile => {
    document.getElementById('nombre-inicio').textContent = profile.name;
    document.getElementById('rol-inicio').textContent = profile.role;
    document.getElementById('email-inicio').textContent = profile.email;
  });
}

function loadSection(hash) {
  // oculta todas
  document.querySelectorAll('main > section')
    .forEach(s => s.hidden = true);

  // marca activo
  document.querySelectorAll('nav a')
    .forEach(a => a.classList.toggle('active',
      a.getAttribute('href') === hash));

  // muestra la pedida
  const sec = document.querySelector(hash);
  if (!sec) return;
  sec.hidden = false;

  if (hash === '#inicio') {
    showProfile();
  } else if (hash === '#wordle' && typeof initWordle === 'function') {
    initWordle();
    document.getElementById('resetButton')
      .addEventListener('click', resetWordle);
  }
}
