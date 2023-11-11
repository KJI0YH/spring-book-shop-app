if (document.getElementById("addTag")) {
    document.getElementById("addTag").addEventListener("click", function () {
        let tagMessage = document.getElementById("tagMessage");
        tagMessage.style.color = "red";

        let tagName = document.getElementById("tagName");
        if (tagName == null || tagName.value === "") {
            tagMessage.textContent = "Name property can not be empty";
            return;
        }

        let tagSlug = document.getElementById("tagSlug");
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
        let tagMessage = document.getElementById("tagMessage");
        tagMessage.style.color = "red";

        let tagName = document.getElementById("tagName");
        if (tagName == null || tagName.value === "") {
            tagMessage.textContent = "Name property can not be empty";
            return;
        }

        let tagSlug = document.getElementById("tagSlug");
        if (tagSlug == null || tagSlug.value === "") {
            tagMessage.textContent = "Slug property can not be empty";
            return;
        }

        const id = document.getElementById("updateTag").dataset.id;

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
        let tagMessage = document.getElementById("tagMessage");
        const id = document.getElementById("deleteTag").dataset.id;
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

if (document.getElementById("addGenre")) {
    document.getElementById("addGenre").addEventListener("click", function () {
        let genreMessage = document.getElementById("genreMessage");
        genreMessage.style.color = "red";

        let genreName = document.getElementById("genreName");
        if (genreName == null || genreName.value === "") {
            genreMessage.textContent = "Name property can not be empty";
            return;
        }

        let genreSlug = document.getElementById("genreSlug");
        if (genreSlug == null || genreSlug.value === "") {
            genreMessage.textContent = "Slug property can not be empty";
            return;
        }

        let genreParent = document.getElementById("genreParent");
        if (genreParent == null || genreParent.value === "") {
            genreMessage.textContent = "Parent property can not be empty";
            return;
        }

        axios.post("/api/cms/genre", {
            parentId: genreParent.value == -1 ? null : genreParent.value,
            name: genreName.value,
            slug: genreSlug.value
        })
            .then(function (response) {
                genreMessage.style.color = "green";
                genreMessage.textContent = JSON.stringify(response.data);
                genreName.value = '';
                genreSlug.value = '';
                genreParent.value = -1;
            })
            .catch(function (error) {
                genreMessage.style.color = "red";
                genreMessage.textContent = error.response.data.error;
            });
    });
}

if (document.getElementById("updateGenre")) {
    document.getElementById("updateGenre").addEventListener("click", function () {
        let genreMessage = document.getElementById("genreMessage");
        genreMessage.style.color = "red";

        let genreName = document.getElementById("genreName");
        if (genreName == null || genreName.value === "") {
            genreMessage.textContent = "Name property can not be empty";
            return;
        }

        let genreSlug = document.getElementById("genreSlug");
        if (genreSlug == null || genreSlug.value === "") {
            genreMessage.textContent = "Slug property can not be empty";
            return;
        }

        let genreParent = document.getElementById("genreParent");
        if (genreParent == null || genreParent.value === "") {
            genreMessage.textContent = "Parent property can not be empty";
            return;
        }

        const id = document.getElementById("updateGenre").dataset.id;

        axios.put("/api/cms/genre/" + id, {
            parentId: genreParent.value == -1 ? null : genreParent.value,
            name: genreName.value,
            slug: genreSlug.value
        })
            .then(function (response) {
                window.location.replace("/genres/" + response.data.slug);
                genreMessage.style.color = "green";
                genreMessage.textContent = JSON.stringify(response.data);
            })
            .catch(function (error) {
                genreMessage.style.color = "red";
                genreMessage.textContent = error.response.data.error;
            });
    });
}

if (document.getElementById("deleteGenre")) {
    document.getElementById("deleteGenre").addEventListener("click", function () {
        let genreMessage = document.getElementById("genreMessage");
        const id = document.getElementById("deleteGenre").dataset.id;

        axios.delete("/api/cms/genre/" + id)
            .then(function (response) {
                window.location.replace("/genres");
            })
            .catch(function (error) {
                genreMessage.style.color = "red";
                genreMessage.textContent = error.response.data.error;
            });
    });
}

if (document.getElementById("addAuthor")) {
    document.getElementById("addAuthor").addEventListener("click", function () {
        let authorMessage = document.getElementById("authorMessage");
        authorMessage.style.color = "red";

        let authorFirstName = document.getElementById("authorFirstName");
        if (authorFirstName == null || authorFirstName.value === "") {
            authorMessage.textContent = "First name property can not be empty";
            return;
        }

        let authorLastName = document.getElementById("authorLastName");
        if (authorLastName == null || authorLastName.value === "") {
            authorMessage.textContent = "Last name property can not be empty";
            return;
        }

        let authorSlug = document.getElementById("authorSlug");
        if (authorSlug == null || authorSlug.value === "") {
            authorMessage.textContent = "Slug property can not be empty";
            return;
        }

        let authorDescription = document.getElementById("authorDescription");

        axios.post("/api/cms/author", {
            firstName: authorFirstName.value,
            lastName: authorLastName.value,
            slug: authorSlug.value,
            description: authorDescription.value
        })
            .then(function (response) {
                authorMessage.style.color = "green";
                authorMessage.textContent = JSON.stringify(response.data);
                authorFirstName.value = '';
                authorLastName.value = '';
                authorSlug.value = '';
                authorDescription.value = '';
            })
            .catch(function (error) {
                authorMessage.style.color = "red";
                authorMessage.textContent = error.response.data.error;
            });
    });
}

if (document.getElementById("updateAuthor")) {
    document.getElementById("updateAuthor").addEventListener("click", function () {
        let authorMessage = document.getElementById("authorMessage");
        authorMessage.style.color = "red";

        let authorFirstName = document.getElementById("authorFirstName");
        if (authorFirstName == null || authorFirstName.value === "") {
            authorMessage.textContent = "First name property can not be empty";
            return;
        }

        let authorLastName = document.getElementById("authorLastName");
        if (authorLastName == null || authorLastName.value === "") {
            authorMessage.textContent = "Last name property can not be empty";
            return;
        }

        let authorSlug = document.getElementById("authorSlug");
        if (authorSlug == null || authorSlug.value === "") {
            authorMessage.textContent = "Slug property can not be empty";
            return;
        }

        let authorDescription = document.getElementById("authorDescription");
        
        const id = document.getElementById("updateAuthor").dataset.id;
        
        axios.put("/api/cms/author/" + id, {
            firstName: authorFirstName.value,
            lastName: authorLastName.value,
            slug: authorSlug.value,
            description: authorDescription.value
        })
            .then(function (response) {
                window.location.replace("/authors/" + response.data.slug);
                authorMessage.style.color = "green";
                authorMessage.textContent = JSON.stringify(response.data);
            })
            .catch(function (error) {
                authorMessage.style.color = "red";
                authorMessage.textContent = error.response.data.error;
            });
    });
}

if (document.getElementById("deleteAuthor")) {
    document.getElementById("deleteAuthor").addEventListener("click", function () {
        let authorMessage = document.getElementById("authorMessage");
        const id = document.getElementById("deleteAuthor").dataset.id;

        axios.delete("/api/cms/author/" + id)
            .then(function (response) {
                window.location.replace("/authors");
            })
            .catch(function (error) {
                authorMessage.style.color = "red";
                authorMessage.textContent = error.response.data.error;
            });
    });
}