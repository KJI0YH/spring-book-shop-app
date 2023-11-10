if (document.getElementById("addTag")) {
    document.getElementById("addTag").addEventListener("click", function () {
        var tagMessage = document.getElementById("tagMessage");
        tagMessage.style.color = "red";

        var tagName = document.getElementById("tagName");
        if (tagName == null || tagName.value === "") {
            tagMessage.textContent = "Name property can not be empty";
            return;
        }

        var tagSlug = document.getElementById("tagSlug");
        if (tagSlug == null || tagSlug.value === "") {
            tagMessage.textContent = "Slug property can not be empty";
            return;
        }

        axios.post("/api/cms/tag", {name: tagName.value, slug: tagSlug.value})
            .then(function (response) {
                tagMessage.style.color = "green";
                tagMessage.textContent = JSON.stringify(response.data);
                tagName.value = '';
                tagSlug.value = '';
            })
            .catch(function (error) {
                tagMessage.style.color = "red";
                tagMessage.textContent = error.response.data.error;
            });
    });
}

if (document.getElementById("updateTag")) {
    document.getElementById("updateTag").addEventListener("click", function () {
        var tagMessage = document.getElementById("tagMessage");
        tagMessage.style.color = "red";

        var tagName = document.getElementById("tagName");
        if (tagName == null || tagName.value === "") {
            tagMessage.textContent = "Name property can not be empty";
            return;
        }

        var tagSlug = document.getElementById("tagSlug");
        if (tagSlug == null || tagSlug.value === "") {
            tagMessage.textContent = "Slug property can not be empty";
            return;
        }
        
        var id = document.getElementById("updateTag").dataset.id;

        axios.put("/api/cms/tag/" + id, {name: tagName.value, slug: tagSlug.value})
            .then(function (response) {
                window.location.replace("/tags/" + response.data.slug);
                tagMessage.style.color = "green";
                tagMessage.textContent = JSON.stringify(response.data);
            })
            .catch(function (error) {
                tagMessage.style.color = "red";
                tagMessage.textContent = error.response.data.error;
            });
    });
}

if (document.getElementById("deleteTag")) {
    document.getElementById("deleteTag").addEventListener("click", function () {
        var id = document.getElementById("deleteTag").dataset.id;
        axios.delete("/api/cms/tag/" + id)
            .then(function (response) {
                window.location.replace("/");
            })
            .catch(function (error) {
                tagMessage.style.color = "red";
                tagMessage.textContent = error.response.data.error;
            });
    });
}