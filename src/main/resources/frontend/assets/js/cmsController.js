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

if (document.getElementById("addBook")) {
    document.getElementById("addBook").addEventListener("click", function () {
        let bookMessage = document.getElementById("bookMessage");
        bookMessage.style.color = "red";

        let bookTitle = document.getElementById("bookTitle");
        if (bookTitle == null || bookTitle.value === "") {
            bookMessage.textContent = "Title property can not be empty";
            return;
        }

        let bookSlug = document.getElementById("bookSlug");
        if (bookSlug == null || bookSlug.value === "") {
            bookMessage.textContent = "Slug property can not be empty";
            return;
        }

        let bookPrice = document.getElementById("bookPrice");
        if (bookPrice == null || bookPrice.valueAsNumber <= 0) {
            bookMessage.textContent = "Price property can not be less or equal than 0";
            return;
        }

        let bookDiscount = document.getElementById("bookDiscount");
        if (bookDiscount == null || bookDiscount.valueAsNumber < 0 || bookDiscount.valueAsNumber > 99) {
            bookMessage.textContent = "Discount property can not be less than 0 and more then 99";
            return;
        }

        let bookBestseller = document.getElementById("bookBestseller");
        if (bookBestseller == null) {
            bookMessage.textContent = "Bestseller property can not be null";
            return;
        }

        let bookPubDate = document.getElementById("bookPubDate");
        if (bookPubDate == null || bookPubDate.value === "") {
            bookMessage.textContent = "Publication date can not be empty";
            return;
        }

        let bookDescription = document.getElementById("bookDescription");

        let authors = document.getElementById("bookAuthors");
        let genres = document.getElementById("bookGenres");
        let tags = document.getElementById("bookTags");

        axios.post("/api/cms/book", {
            title: bookTitle.value,
            slug: bookSlug.value,
            image: "",
            price: parseInt(bookPrice.valueAsNumber * 100),
            discount: bookDiscount.valueAsNumber,
            isBestseller: bookBestseller.checked,
            pubDate: bookPubDate.value,
            description: bookDescription.value,
            authorIds: Array.from(authors.selectedOptions).map((author, index) => ({
                authorId: parseInt(author.value),
                sortIndex: index,
            })),
            genreIds: Array.from(genres.selectedOptions).map(genre => parseInt(genre.value)),
            tagIds: Array.from(tags.selectedOptions).map(tag => parseInt(tag.value)),
        })
            .then(function (response) {
                bookMessage.style.color = "green";
                bookMessage.textContent = JSON.stringify(response.data);
                bookTitle.value = '';
                bookSlug.value = '';
                bookPrice.value = '';
                bookDiscount.value = '';
                bookBestseller.checked = false;
                bookDescription = '';
                bookPubDate.value = '';
                authors.selectedOptions = '';
                genres.selectedOptions = '';
                tags.selectedOptions = '';
            })
            .catch(function (error) {
                bookMessage.style.color = "red";
                bookMessage.textContent = error.response.data.error;
            })
    });
}

if (document.getElementById("updateBook")) {
    document.getElementById("updateBook").addEventListener("click", function () {
        let bookMessage = document.getElementById("bookMessage");
        bookMessage.style.color = "red";

        let bookTitle = document.getElementById("bookTitle");
        if (bookTitle == null || bookTitle.value === "") {
            bookMessage.textContent = "Title property can not be empty";
            return;
        }

        let bookSlug = document.getElementById("bookSlug");
        if (bookSlug == null || bookSlug.value === "") {
            bookMessage.textContent = "Slug property can not be empty";
            return;
        }

        let bookPrice = document.getElementById("bookPrice");
        if (bookPrice == null || bookPrice.valueAsNumber <= 0) {
            bookMessage.textContent = "Price property can not be less or equal than 0";
            return;
        }

        let bookDiscount = document.getElementById("bookDiscount");
        if (bookDiscount == null || bookDiscount.valueAsNumber < 0 || bookDiscount.valueAsNumber > 99) {
            bookMessage.textContent = "Discount property can not be less than 0 and more then 99";
            return;
        }

        let bookBestseller = document.getElementById("bookBestseller");
        if (bookBestseller == null) {
            bookMessage.textContent = "Bestseller property can not be null";
            return;
        }

        let bookPubDate = document.getElementById("bookPubDate");
        if (bookPubDate == null || bookPubDate.value === "") {
            bookMessage.textContent = "Publication date can not be empty";
            return;
        }

        let bookDescription = document.getElementById("bookDescription");

        const id = document.getElementById("updateBook").dataset.id;

        axios.put("/api/cms/book/" + id, {
            title: bookTitle.value,
            slug: bookSlug.value,
            price: parseInt(bookPrice.valueAsNumber * 100),
            discount: bookDiscount.valueAsNumber,
            isBestseller: bookBestseller.checked,
            pubDate: bookPubDate.value,
            description: bookDescription.value,
        })
            .then(function (response) {
                window.location.replace("/books/" + response.data.slug);
                bookMessage.style.color = "green";
                bookMessage.textContent = JSON.stringify(response.data);
            })
            .catch(function (error) {
                bookMessage.style.color = "red";
                bookMessage.textContent = error.response.data.error;
            })
    });
}

if (document.getElementById("deleteBook")) {
    document.getElementById("deleteBook").addEventListener("click", function () {
        let bookMessage = document.getElementById("bookMessage");
        bookMessage.style.color = "red";

        const id = document.getElementById("deleteBook").dataset.id;

        axios.delete("/api/cms/book/" + id)
            .then(function (response) {
                window.location.replace("/");
            })
            .catch(function (error) {
                bookMessage.style.color = "red";
                bookMessage.textContent = error.response.data.error;
            })
    });
}

if (document.getElementById("addBook2Tag")) {
    document.getElementById("addBook2Tag").addEventListener("click", function () {
        const bookId = document.getElementById("addBook2Tag").dataset.bookid;
        let tags = document.getElementById("bookTags");

        axios.post("/api/cms/book/tag", {
            bookIds: [bookId],
            tagIds: Array.from(tags.selectedOptions).map(tag => parseInt(tag.value))
        })
            .then(function (response) {
                window.location.reload();
            });
    });
}

if (document.getElementById("deleteBook2Tag")) {
    let deleteButtons = document.querySelectorAll('#deleteBook2Tag');
    deleteButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            const bookId = button.getAttribute('data-bookId');
            const tagId = button.getAttribute('data-tagId');

            axios.delete("/api/cms/book/" + bookId + "/tag/" + tagId)
                .then(function (response) {
                    window.location.reload();
                });
        });
    });
}


if (document.getElementById("addBook2Genre")) {
    document.getElementById("addBook2Genre").addEventListener("click", function () {
        const bookId = document.getElementById("addBook2Genre").dataset.bookid;
        let genres = document.getElementById("bookGenres");

        axios.post("/api/cms/book/genre", {
            bookIds: [bookId],
            genreIds: Array.from(genres.selectedOptions).map(genre => parseInt(genre.value))
        })
            .then(function (response) {
                window.location.reload();
            });
    });
}

if (document.getElementById("deleteBook2Genre")) {
    let deleteButtons = document.querySelectorAll('#deleteBook2Genre');
    deleteButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            const bookId = button.getAttribute('data-bookId');
            const genreId = button.getAttribute('data-genreId');

            axios.delete("/api/cms/book/" + bookId + "/genre/" + genreId)
                .then(function (response) {
                    window.location.reload();
                })
        });
    });
}

if (document.getElementById("addBook2Author")) {
    document.getElementById("addBook2Author").addEventListener("click", function () {
        const bookId = document.getElementById("addBook2Author").dataset.bookid;
        let authors = document.getElementById("bookAuthors");

        axios.post("/api/cms/book/author", {
            bookIds: [bookId],
            authors: Array.from(authors.selectedOptions).map((author, index) => ({
                authorId: parseInt(author.value),
                sortIndex: index,
            })),
        })
            .then(function (response) {
                window.location.reload();
            });
    });
}

if (document.getElementById("updateBook2Author")) {
    let updateButtons = document.querySelectorAll('#updateBook2Author');
    updateButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            const bookId = button.getAttribute('data-bookId');
            const authorId = button.getAttribute('data-authorId');
            const sortIndex = document.getElementById("sortIndex-" + authorId).valueAsNumber;

            axios.put("/api/cms/book/author", {
                bookIds: [bookId],
                authors: [{
                    authorId: authorId,
                    sortIndex: sortIndex,
                }]
            })
                .then(function (response) {
                    window.location.reload();
                })
        });
    });
}

if (document.getElementById("deleteBook2Author")) {
    let deleteButtons = document.querySelectorAll('#deleteBook2Author');
    deleteButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            const bookId = button.getAttribute('data-bookId');
            const authorId = button.getAttribute('data-authorId');

            axios.delete("/api/cms/book/" + bookId + "/author/" + authorId)
                .then(function (response) {
                    window.location.reload();
                })
        });
    });
}

if (document.getElementById("updateReview")) {
    let updateReview = document.querySelectorAll('#updateReview');
    updateReview.forEach(function (button) {
        button.addEventListener('click', function () {
            const id = button.getAttribute('data-id');
            const text = document.getElementById("reviewText-" + id).value;

            axios.put("/api/cms/bookReview/" + id, {
                text: text
            })
                .then(function (response) {
                    window.location.reload();
                })
        });
    });
}

if (document.getElementById("deleteReview")) {
    let deleteButtons = document.querySelectorAll('#deleteReview');
    deleteButtons.forEach(function (button) {
        button.addEventListener('click', function () {
            const id = button.getAttribute('data-id');

            axios.delete("/api/cms/bookReview/" + id)
                .then(function (response) {
                    window.location.reload();
                })
        });
    });
}