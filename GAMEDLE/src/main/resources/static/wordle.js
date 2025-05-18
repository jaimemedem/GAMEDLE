
const allowedTags = new Set(["n", "adj", "v"]);

const maxIntentos = 6;
let intentosRestantes = maxIntentos;
let intentoActual = [];  // Almacena las letras del intento actual
let posLetra = 0;
let palabraSecreta = "";

function iniciarTablero() {
    const tablero = document.getElementById("tablero-juego");
    tablero.innerHTML = ""; // Limpia el contenido previo
    for (let i = 0; i < maxIntentos; i++) {
        const fila = document.createElement("div");
        fila.classList.add("fila-letras");
        for (let j = 0; j < 5; j++) {
            const celda = document.createElement("div");
            celda.classList.add("caja-letra");
            fila.appendChild(celda);
        }
        tablero.appendChild(fila);
    }
}

// Función para colorear las teclas del teclado virtual
function colorearTecla(letra, color) {
    const teclas = document.getElementsByClassName("tecla");
    for (const btn of teclas) {
        if (btn.textContent === letra) {
            const colorActual = btn.style.backgroundColor;
            if (colorActual === "green") return;
            if (colorActual === "yellow" && color !== "green") return;
            btn.style.backgroundColor = color;
            break;
        }
    }
}

// Función para borrar la última letra ingresada
function quitarLetra() {
    const filaActual = document.getElementsByClassName("fila-letras")[maxIntentos - intentosRestantes];
    if (posLetra > 0) {
        const celda = filaActual.children[posLetra - 1];
        celda.textContent = "";
        celda.classList.remove("letra-rellena");
        intentoActual.pop();
        posLetra--;
    }
}

/*
  Función para verificar si una palabra existe EXACTAMENTE y
  que sea de las categorías permitidas (sustantivo, adjetivo o verbo).
  Se consulta el endpoint /words de Datamuse usando los parámetros:
    sp=<palabra>  : se busca la coincidencia exacta del patrón.
    qe=sp         : se obliga a que la búsqueda sea exacta en ortografía.
    max=1         : se obtiene únicamente el mejor resultado.
    md=p          : se solicitan metadatos con tags de partes del habla.
    v=es          : se limita a resultados en español.
*/
async function existePalabra(palabra) {
    try {
        const url = `https://api.datamuse.com/words?sp=${palabra}&qe=sp&max=1&md=p&v=es`;
        const res = await fetch(url);
        if (!res.ok) throw new Error("Error en la API de Datamuse");
        const datos = await res.json();
        if (datos.length === 0) return false;
        // Se compara la palabra devuelta de forma exacta
        const palabraAPI = datos[0].word.toLowerCase();
        if (palabraAPI !== palabra.toLowerCase()) return false;
        // Se comprueba que los metadatos incluyan alguna parte del habla y que al menos
        // uno de los tags esté en el conjunto de allowedTags.
        if (!datos[0].tags || datos[0].tags.length === 0) return false;
        const tagsLower = datos[0].tags.map(tag => tag.toLowerCase());
        const hasValidTag = [...allowedTags].some(validTag => tagsLower.includes(validTag));
        return hasValidTag;
    } catch (error) {
        console.error("Error al verificar la palabra:", error);
        return false;
    }
}


async function obtenerPalabraAleatoria() {
    try {
        const url = "https://api.datamuse.com/words?sp=?????&max=100&md=p&v=es";
        const res = await fetch(url);
        if (!res.ok) throw new Error("Error en la API al obtener palabra aleatoria");
        const datos = await res.json();
        if (datos.length === 0) throw new Error("No se obtuvieron palabras");
        // Filtra las palabras que tengan al menos un tag permitido
        const palabrasValidas = datos.filter(item => {
            if (!item.tags || item.tags.length === 0) return false;
            const tagsLower = item.tags.map(tag => tag.toLowerCase());
            return [...allowedTags].some(validTag => tagsLower.includes(validTag));
        });
        if (palabrasValidas.length === 0) throw new Error("No se encontraron palabras válidas");
        const indiceAleatorio = Math.floor(Math.random() * palabrasValidas.length);
        return palabrasValidas[indiceAleatorio].word.toLowerCase();
    } catch (error) {
        console.error("Error al obtener palabra aleatoria:", error);
        return "gatos"; // Fallback
    }
}

// Función asincrónica para verificar el intento del usuario
async function verificarPalabra() {
    const filaActual = document.getElementsByClassName("fila-letras")[maxIntentos - intentosRestantes];
    const palabraIngresada = intentoActual.join("");
    if (palabraIngresada.length !== 5) {
        toastr.error("¡Faltan letras!");
        return;
    }
    const existe = await existePalabra(palabraIngresada);
    if (!existe) {
        toastr.error("La palabra no existe o no es sustantivo, adjetivo o verbo!");
        return;
    }

    // Comparación con la palabra secreta
    let copiaSecreta = palabraSecreta.split("");
    for (let i = 0; i < 5; i++) {
        const celda = filaActual.children[i];
        let colorLetra = "";
        const letra = intentoActual[i];
        const pos = copiaSecreta.indexOf(letra);
        if (pos === -1) {
            colorLetra = "grey";
        } else {
            colorLetra = (letra === palabraSecreta[i]) ? "green" : "yellow";
            copiaSecreta[pos] = "#";
        }
        setTimeout(() => {
            celda.style.backgroundColor = colorLetra;
            celda.classList.add("animar-celda");
            colorearTecla(letra, colorLetra);
        }, 250 * i);
    }

    if (palabraIngresada === palabraSecreta) {
        toastr.success("¡Ganaste!");
        intentosRestantes = 0;
        return;
    } else {
        intentosRestantes--;
        intentoActual = [];
        posLetra = 0;
        if (intentosRestantes === 0) {
            toastr.error("Se acabaron los intentos. La palabra era: " + palabraSecreta);
        }
    }
}

// Función para agregar una letra al intento actual
function agregarLetra(letra) {
    if (posLetra < 5) {
        const filaActual = document.getElementsByClassName("fila-letras")[maxIntentos - intentosRestantes];
        const celda = filaActual.children[posLetra];
        celda.textContent = letra.toLowerCase();
        celda.classList.add("letra-rellena");
        intentoActual.push(letra.toLowerCase());
        posLetra++;
    }
}

// Inicialización: se obtiene la palabra secreta y se arma el tablero
obtenerPalabraAleatoria().then(palabra => {
    palabraSecreta = palabra;
    console.log("Palabra secreta:", palabraSecreta);
    iniciarTablero();
});

// Eventos del teclado físico
document.addEventListener("keyup", async (e) => {
    if (intentosRestantes === 0) return;
    const tecla = e.key;
    if (tecla === "Backspace") {
        quitarLetra();
    } else if (tecla === "Enter") {
        await verificarPalabra();
    } else if (/^[a-zA-ZñÑ]$/.test(tecla)) {
        agregarLetra(tecla);
    }
});

window.addEventListener("load", () => {
    const modal = document.getElementById("modalExplicacion");
    const closeButton = document.getElementById("cerrarModal");
    closeButton.addEventListener("click", () => {
        modal.style.display = "none";
    });
});

// Eventos del teclado virtual
document.getElementById("teclado").addEventListener("click", (e) => {
    const objetivo = e.target;
    if (!objetivo.classList.contains("tecla")) return;
    let valorTecla = objetivo.textContent;
    if (valorTecla === "Del") {
        valorTecla = "Backspace";
    }
    document.dispatchEvent(new KeyboardEvent("keyup", { key: valorTecla }));
});



// Función para reiniciar el juego
async function resetGame() {
    // Restablece las variables de juego
    intentosRestantes = maxIntentos;
    posLetra = 0;
    intentoActual = [];

    // Restablece el tablero
    iniciarTablero();

    // Limpia los colores del teclado
    const teclas = document.getElementsByClassName("tecla");
    for (const btn of teclas) {
        btn.style.backgroundColor = "";
    }

    // Obtiene una nueva palabra secreta
    palabraSecreta = await obtenerPalabraAleatoria();
    console.log("Nueva palabra secreta:", palabraSecreta);

    // Opcional: Notifica al usuario
    toastr.info("Juego reiniciado");
}

// Añadimos el evento para el botón de reset, 
// asegurándonos de que el botón tenga el id "resetButton".
document.getElementById("resetButton").addEventListener("click", resetGame);
