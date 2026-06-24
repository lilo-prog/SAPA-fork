(function () {

    function getToken() {
        return localStorage.getItem("accessToken");
    }

    document.addEventListener("DOMContentLoaded", function () {
        cargarForosActivos();

        const form = document.getElementById("formCrearPost");
        form.addEventListener("submit", crearForo);
    });

    async function cargarForosActivos() {
        const contenedor = document.getElementById("listaForos");
        contenedor.innerHTML = `<div class="publicacion">Cargando publicaciones...</div>`;

        try {
            const res = await fetch("/forums/active");

            if (!res.ok) throw new Error("No se pudieron cargar los foros.");

            const foros = await res.json();
            mostrarForos(foros);

        } catch (error) {
            contenedor.innerHTML = `<div class="publicacion">${error.message}</div>`;
        }
    }

    async function buscarForos() {
        const title = document.getElementById("searchInput").value.trim();

        if (title === "") {
            cargarForosActivos();
            return;
        }

        try {
            const res = await fetch("/forums/filter?title=" + encodeURIComponent(title));

            if (!res.ok) throw new Error("No se pudo buscar el foro.");

            const foros = await res.json();
            mostrarForos(foros);

        } catch (error) {
            mostrarMensaje(error.message, "error");
        }
    }

    async function crearForo(e) {
        e.preventDefault();

        const title = document.getElementById("titulo").value.trim();
        const content = document.getElementById("contenido").value.trim();

        if (!title || !content) {
            mostrarMensaje("Completá todos los campos.", "error");
            return;
        }

        const foro = {
            title: title,
            content: content
        };

        try {
            const res = await fetch("/forums", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${getToken()}`
                },
                body: JSON.stringify(foro)
            });

            if (!res.ok) throw new Error("No se pudo crear la publicación.");

            mostrarMensaje("Publicación creada correctamente.", "ok");
            document.getElementById("formCrearPost").reset();
            ocultarFormulario();
            cargarForosActivos();

        } catch (error) {
            mostrarMensaje(error.message, "error");
        }
    }

    function mostrarForos(foros) {
        const contenedor = document.getElementById("listaForos");

        if (!foros || foros.length === 0) {
            contenedor.innerHTML = `
                <div class="publicacion">
                    <p class="empty-state">Todavía no hay publicaciones.</p>
                </div>
            `;
            return;
        }

        contenedor.innerHTML = foros.map(foro => `
            <article class="publicacion" onclick="entrarAlForo(${foro.forumId || foro.id})">
                <span class="tag">Foro</span>
                <h3>${escapeHtml(foro.title)}</h3>
                <p>${escapeHtml(foro.content)}</p>
                <p class="autor">${escapeHtml(foro.authorName || "")}</p>
            </article>
        `).join("");
    }

    function entrarAlForo(id) {
        window.location.href = "foro-detalle.html?id=" + id;
    }

    window.entrarAlForo = entrarAlForo;

    function mostrarFormulario() {
        if (!getToken()) {
            mostrarMensaje("Tenés que iniciar sesión para publicar.", "error");
            return;
        }

        document.getElementById("formCrearPost").style.display = "flex";
    }

    function ocultarFormulario() {
        document.getElementById("formCrearPost").style.display = "none";
    }

    function mostrarMensaje(texto, tipo) {
        const mensaje = document.getElementById("mensaje");
        mensaje.textContent = texto;
        mensaje.className = "mensaje " + tipo;
    }

    function escapeHtml(str) {
        const div = document.createElement("div");
        div.textContent = str ?? "";
        return div.innerHTML;
    }

    window.buscarForos = buscarForos;
    window.cargarForosActivos = cargarForosActivos;
    window.mostrarFormulario = mostrarFormulario;
    window.ocultarFormulario = ocultarFormulario;

})();