async function parseResponse(response, fallbackMessage) {
  if (response.ok) {
    if (response.status === 204) return null;
    return response.json();
  }

  let message = fallbackMessage;
  try {
    const body = await response.json();
    message = body.message || fallbackMessage;
  } catch {}

  throw new Error(message);
}

export function getJson(url, fallbackMessage) {
  return fetch(url).then((response) => parseResponse(response, fallbackMessage));
}

export function sendJson(url, options, fallbackMessage) {
  return fetch(url, options).then((response) => parseResponse(response, fallbackMessage));
}

export function sendVoid(url, options, fallbackMessage) {
  return fetch(url, options).then((response) => parseResponse(response, fallbackMessage));
}
