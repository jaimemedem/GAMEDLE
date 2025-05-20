const maxIntentos = 6;
let intentosRestantes = maxIntentos;
let intentoActual = [];
let posLetra = 0;
let palabraSecreta = "";

function iniciarTablero() {
    const tablero = document.getElementById("tablero-juego");
    tablero.innerHTML = "";
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

async function obtenerPalabraAleatoria() {
    try {
        const res = await fetch("/api/wordle/today");
        if (!res.ok) throw new Error("Error al obtener palabra del día");
        const data = await res.json();
        return data.word.toLowerCase();
    } catch (error) {
        console.error("Error al obtener palabra del día:", error);
        return "gatos"; // Fallback
    }
}

async function verificarPalabra() {
    const filaActual = document.getElementsByClassName("fila-letras")[maxIntentos - intentosRestantes];
    const palabraIngresada = intentoActual.join("");
    if (palabraIngresada.length !== 5) {
        toastr.error("¡Faltan letras!");
        return;
    }

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

obtenerPalabraAleatoria().then(palabra => {
    palabraSecreta = palabra;
    console.log("Palabra secreta:", palabraSecreta);
    iniciarTablero();
});

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

document.getElementById("teclado").addEventListener("click", (e) => {
    const objetivo = e.target;
    if (!objetivo.classList.contains("tecla")) return;
    let valorTecla = objetivo.textContent;
    if (valorTecla === "Del") {
        valorTecla = "Backspace";
    }
    document.dispatchEvent(new KeyboardEvent("keyup", { key: valorTecla }));
});

async function resetGame() {
    intentosRestantes = maxIntentos;
    posLetra = 0;
    intentoActual = [];

    iniciarTablero();

    const teclas = document.getElementsByClassName("tecla");
    for (const btn of teclas) {
        btn.style.backgroundColor = "";
    }

    palabraSecreta = await obtenerPalabraAleatoria();
    console.log("Nueva palabra secreta:", palabraSecreta);
    toastr.info("Juego reiniciado");
}

document.getElementById("resetButton").addEventListener("click", resetGame);
