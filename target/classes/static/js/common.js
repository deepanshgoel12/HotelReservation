// Simple storage helpers
const storage = {
    set(key, value) { localStorage.setItem(key, JSON.stringify(value)); },
    get(key, fallback = null) { try { return JSON.parse(localStorage.getItem(key)) ?? fallback } catch { return fallback } },
    remove(key) { localStorage.removeItem(key); }
};

function qs(sel, parent = document) { return parent.querySelector(sel); }
function qsa(sel, parent = document) { return [...parent.querySelectorAll(sel)]; }

function setAlert(container, type, message) {
    if (!container) return;
    container.innerHTML = `<div class="alert ${type}">${message}</div>`;
}

function formatDate(value) {
    const d = new Date(value);
    return d.toISOString().slice(0, 10);
}

export { storage, qs, qsa, setAlert, formatDate };
