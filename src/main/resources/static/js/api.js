// Base API setup (same-origin)
const API_BASE = '/api';

async function apiRequest(path, options = {}) {
    const headers = { 'Content-Type': 'application/json', ...(options.headers || {}) };

    const response = await fetch(`${API_BASE}${path}`, { headers, ...options });
    const data = await response.json().catch(() => ({}));

    if (!response.ok) {
        const message = data.message || `Request failed: ${response.status}`;
        throw new Error(message);
    }
    return data;
}

export { API_BASE, apiRequest };
