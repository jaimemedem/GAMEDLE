// --- Gestión de sesión y perfil ----
function datosPerfil() {
  return fetch('/api/users/me', { credentials: 'include' })
    .then(res => {
      if (!res.ok) throw new Error('No autenticado');
      return res.json();
    });
}

function articuloInicio() {
  return datosPerfil().then(perfil => {
    document.getElementById('nombre-inicio').textContent = perfil.name;
    document.getElementById('tel-inicio').textContent = perfil.role;
    document.getElementById('email-inicio').textContent = perfil.email;
  });
}

function salir() {
  fetch('/api/users/me/session', { method: 'DELETE', credentials: 'include' })
    .then(() => location.href = 'index.html');
}

function baja() {
  if (confirm("Esto borrará tu usuario, ¿estás seguro?")) {
    fetch('/api/users/me', { method: 'DELETE', credentials: 'include' })
      .then(() => location.href = 'index.html');
  }
}

// --- Navegación entre vistas ----
window.addEventListener('hashchange', inicializar);
window.addEventListener('DOMContentLoaded', inicializar);

function inicializar() {
  // Oculta todas las secciones
  document.querySelectorAll('article').forEach(a => a.hidden = true);
  // Desactiva todos los enlaces
  document.querySelectorAll('nav a').forEach(a => a.classList.remove('active'));

  const articulo = location.hash || '#inicio';
  cargarArticulo(articulo)
    .then(() => mostrarArticulo(articulo))
    .catch(err => {
      console.error(err);
      mostrarArticulo('#inicio');
    });
}

function cargarArticulo(articulo) {
  switch (articulo) {
    case '#inicio':
      return articuloInicio();
    case '#wordle':
      // Si tu wordle es una página aparte no necesitas nada aquí
      return Promise.resolve();
    case '#leaderboard':
      return fetchLeaderboard('wordle');
    default:
      return articuloInicio();
  }
}

function mostrarArticulo(articulo) {
  const section = document.querySelector(articulo);
  if (section) section.hidden = false;
  const link = document.querySelector(`nav a[href="${articulo}"]`);
  if (link) link.classList.add('active');
}

// --- Leaderboard ----
function fetchLeaderboard(game) {
  return fetch(`/api/${game}/stats/leaderboard`, {
    credentials: 'include'
  })
  .then(res => {
    if (!res.ok) throw new Error('Error al cargar leaderboard: ' + res.status);
    return res.json();
  })
  .then(data => loadLeaderboardTable(data));
}

function loadLeaderboardTable(data) {
  const tbody = document.querySelector('#leaderboardTable tbody');
  tbody.innerHTML = '';
  data.forEach((item, idx) => {
    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td>${idx + 1}</td>
      <td>${item.user}</td>
      <td>${(item.success_rate * 100).toFixed(2)}%</td>
      <td>${item.gamesPlayed}</td>
    `;
    tbody.appendChild(tr);
  });
}
