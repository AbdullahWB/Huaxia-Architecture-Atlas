// Huaxia Atlas - UI helpers (toasts, tooltips, modals)

(function () {
  const toastStack = document.getElementById("hx-toast-stack");

  const pushToast = (message, type = "info") => {
    if (!toastStack || !message) return;
    const toast = document.createElement("div");
    toast.className = `hx-toast ${type}`;
    toast.textContent = message.trim();
    toastStack.appendChild(toast);
    setTimeout(() => toast.remove(), 4600);
  };

  // Convert server alerts into toasts
  document.querySelectorAll("[data-toast]").forEach((alert) => {
    const type = alert.dataset.toast || "info";
    pushToast(alert.textContent, type);
    alert.remove();
  });

  // Bootstrap tooltips (for small popups)
  if (window.bootstrap && document.querySelector("[data-bs-toggle='tooltip']")) {
    const triggers = [].slice.call(
      document.querySelectorAll("[data-bs-toggle='tooltip']")
    );
    triggers.forEach((el) => new bootstrap.Tooltip(el));
  }

  // Optional: confirm on any form with data-confirm attribute
  document.querySelectorAll("form[data-confirm]").forEach((form) => {
    form.addEventListener("submit", (e) => {
      const msg = form.getAttribute("data-confirm") || "Are you sure?";
      if (!confirm(msg)) e.preventDefault();
    });
  });

  // Focus the first input on login page (nice UX)
  const first = document.querySelector("form input");
  if (first) first.focus();
})();
