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

  // Chat: scroll to the latest message
  const chatLog = document.querySelector("[data-chat-scroll]");
  if (chatLog) {
    setTimeout(() => {
      chatLog.scrollTop = chatLog.scrollHeight;
    }, 0);
  }

  // AI explain modal
  const explainModalEl = document.getElementById("hxExplainModal");
  if (explainModalEl && window.bootstrap) {
    const explainModal = new bootstrap.Modal(explainModalEl);
    const titleEl = document.getElementById("hxExplainModalLabel");
    const metaEl = document.getElementById("hxExplainModalMeta");
    const bodyEl = document.getElementById("hxExplainModalBody");

    const handleExplain = async (button) => {
      const type = button.dataset.aiType;
      const id = button.dataset.aiId;
      const title = button.dataset.aiTitle || "AI explanation";
      const label = button.dataset.aiLabel || "AI summary";

      if (!type || !id || !titleEl || !metaEl || !bodyEl) {
        pushToast("AI explanation is not available for this item.", "error");
        return;
      }

      const originalLabel = button.textContent;
      button.disabled = true;
      button.textContent = "Loading...";

      titleEl.textContent = `AI explain: ${title}`;
      metaEl.textContent = `Generating ${label.toLowerCase()}...`;
      bodyEl.textContent = "Loading AI response...";
      explainModal.show();

      try {
        const response = await fetch(`/api/ai/explain/${type}/${id}`, {
          headers: {
            Accept: "application/json"
          }
        });

        if (!response.ok) {
          throw new Error(`Request failed (${response.status})`);
        }

        const data = await response.json();
        titleEl.textContent = data.title ? `AI explain: ${data.title}` : "AI explanation";
        metaEl.textContent = label;
        bodyEl.textContent = data.summary || "No summary was returned.";
      } catch (err) {
        const message = err && err.message ? err.message : "Unable to load AI summary.";
        bodyEl.textContent = message;
        pushToast(message, "error");
      } finally {
        button.disabled = false;
        button.textContent = originalLabel;
      }
    };

    document.querySelectorAll("[data-ai-explain]").forEach((button) => {
      button.addEventListener("click", () => handleExplain(button));
    });
  }

  // User message modal (dashboard)
  const userMessageModalEl = document.getElementById("hxUserMessageModal");
  if (userMessageModalEl && window.bootstrap) {
    const subjectEl = document.getElementById("hxUserMessageModalLabel");
    const metaEl = document.getElementById("hxUserMessageMeta");
    const bodyEl = document.getElementById("hxUserMessageBody");
    const adminEl = document.getElementById("hxUserMessageAdmin");
    const adminWrap = document.getElementById("hxUserMessageAdminWrap");
    const aiEl = document.getElementById("hxUserMessageAi");
    const aiWrap = document.getElementById("hxUserMessageAiWrap");

    const setText = (el, value) => {
      if (!el) return;
      el.textContent = value || "";
    };

    document.querySelectorAll("[data-message-open]").forEach((button) => {
      button.addEventListener("click", () => {
        const subject = button.dataset.messageSubject || "Message";
        const date = button.dataset.messageDate || "";
        const body = button.dataset.messageBody || "";
        const admin = button.dataset.messageAdmin || "";
        const ai = button.dataset.messageAi || "";

        setText(subjectEl, subject || "Message");
        setText(metaEl, date ? `Received ${date}` : "Message details");
        setText(bodyEl, body || "No message content.");
        setText(adminEl, admin);
        setText(aiEl, ai);

        if (adminWrap) {
          adminWrap.style.display = admin ? "" : "none";
        }
        if (aiWrap) {
          aiWrap.style.display = ai ? "" : "none";
        }
      });
    });
  }

})();
