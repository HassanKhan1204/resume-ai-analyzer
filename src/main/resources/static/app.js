const form = document.querySelector("#analysisForm");
const fileInput = document.querySelector("#resumeFile");
const fileName = document.querySelector("#fileName");
const formMessage = document.querySelector("#formMessage");
const submitButton = document.querySelector("#submitButton");
const scoreBadge = document.querySelector("#scoreBadge");
const resultSubtitle = document.querySelector("#resultSubtitle");
const missingSkills = document.querySelector("#missingSkills");
const suggestions = document.querySelector("#suggestions");
const summary = document.querySelector("#summary");
const resumeId = document.querySelector("#resumeId");
const apiStatus = document.querySelector("#apiStatus");

fileInput.addEventListener("change", () => {
    fileName.textContent = fileInput.files[0]?.name || "No file selected";
});

form.addEventListener("submit", async (event) => {
    event.preventDefault();
    formMessage.className = "message";
    formMessage.textContent = "Analyzing resume...";
    submitButton.disabled = true;
    apiStatus.textContent = "Working";

    const formData = new FormData();
    formData.append("file", fileInput.files[0]);
    formData.append("targetJobDescription", document.querySelector("#targetJobDescription").value);

    document.querySelector("#requiredSkills").value
        .split(",")
        .map((skill) => skill.trim())
        .filter(Boolean)
        .forEach((skill) => formData.append("requiredSkills", skill));

    try {
        const response = await fetch("/api/resumes/analyze", {
            method: "POST",
            body: formData
        });

        if (!response.ok) {
            const problem = await response.json().catch(() => ({}));
            throw new Error(problem.detail || `Request failed with status ${response.status}`);
        }

        const data = await response.json();
        renderResult(data);
        formMessage.textContent = "Analysis complete.";
        apiStatus.textContent = "API ready";
    } catch (error) {
        formMessage.className = "message error";
        formMessage.textContent = error.message;
        apiStatus.textContent = "Check API";
    } finally {
        submitButton.disabled = false;
    }
});

function renderResult(data) {
    const analysis = data.analysis;
    scoreBadge.textContent = analysis.jobMatchingScore;
    resultSubtitle.textContent = `${data.originalFilename} analyzed with ${analysis.analyzer}`;
    summary.textContent = analysis.summary;
    resumeId.textContent = `resumeId: ${data.id}`;

    renderList(missingSkills, analysis.missingSkills, "No missing skills detected.", "missing");
    renderList(suggestions, analysis.suggestions, "No suggestions returned.", "");
}

function renderList(container, items, emptyText, className) {
    container.replaceChildren();
    const values = items && items.length ? items : [emptyText];
    values.forEach((item) => {
        const li = document.createElement("li");
        li.textContent = item;
        if (className) {
            li.className = className;
        }
        container.appendChild(li);
    });
}
