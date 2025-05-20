
function fetchStats(game) {
  fetch("http://localhost:8080/api/wordle/stats", {
    credentials: 'include'
  })
  .then(response => response.text())
  .then(text => {
    if (text.trim() === "") {
      console.log("Respuesta vacía");
      return;
    }
    const data = JSON.parse(text);
    loadTable([data]);
  })
  .catch(error => {
    console.error('Error al cargar estadísticas:', error);
  });
}


function loadTable(data) {
  const tbody = document.querySelector("#statsTable tbody");
  if (!tbody) return console.warn('No se encontró la tabla #statsTable');
  tbody.innerHTML = "";

  data.forEach(item => {
    const tr = document.createElement("tr");
    tr.innerHTML = `
      <td>${item.user}</td>
      <td>${item.gamename}</td>
      <td>${item.attempts_pg.toFixed(2)}</td>
      <td>${(item.success_rate * 100).toFixed(2)}%</td>
    `;
    tbody.appendChild(tr);
  });
}


window.addEventListener('DOMContentLoaded', () => {
  fetchStats('wordle');
});