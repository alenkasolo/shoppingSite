//submit and display comment
let form = document.getElementById("form");
let commentSection = document.getElementById("comments-container");
if (form != null) {
    form.addEventListener("submit", event => {
        event.preventDefault();
        let loaderElement = loader(form);
        form.classList.add("disable-form");
        fetch("/comment", {
            method: "post",
            body: new FormData(form)
        }).then(response => {
            form.removeChild(loaderElement);
            form.classList.remove("disable-form");
            if (response.status === 200) {
                response.json().then(body => {
                    let userName = document.createElement("b");
                    let newLine = document.createElement("br");
                    let comment = document.createElement("div");
                    let userComment = document.createElement("div");
                    let commentButton = document.createElement("div");
                    let deleteCommentButton = document.createElement("button");
                    let updateCommentButton = document.createElement("button");

                    let userCommentText = document.createTextNode(body.comment);
                    let userNameText = document.createTextNode(body.user);
                    let updateCommentButtonText = document.createTextNode("update comment");
                    let deleteCommentButtonText = document.createTextNode("delete comment");

                    form.elements.namedItem("message").value = "";
                    comment.classList.add("comment");
                    userName.classList.add("user-name");
                    userComment.classList.add("user-comment");
                    commentButton.classList.add("comment-button");
                    deleteCommentButton.name = "deleteComment";
                    deleteCommentButton.type = "button";
                    deleteCommentButton.value = body.commentId;
                    updateCommentButton.name = "updateComment";
                    updateCommentButton.type = "button";
                    updateCommentButton.value = body.commentId;

                    userName.appendChild(userNameText);
                    userComment.appendChild(userCommentText);
                    deleteCommentButton.appendChild(deleteCommentButtonText);
                    deleteCommentButton.addEventListener("click", deleteComment);
                    updateCommentButton.appendChild(updateCommentButtonText);
                    updateCommentButton.addEventListener("click", updateComment);

                    commentButton.appendChild(deleteCommentButton);
                    commentButton.appendChild(updateCommentButton);

                    comment.appendChild(userName);
                    comment.append(newLine);
                    comment.appendChild(userComment);
                    comment.appendChild(commentButton);

                    commentSection.append(comment);
                });
            }
        });
    });
}

//update and delete comments
let updateButtons = document.getElementsByName("updateComment");
let deleteButtons = document.getElementsByName("deleteComment");

for (let updateButton of updateButtons) {
    updateButton.addEventListener("click", updateComment);
}
for (let deleteButton of deleteButtons) {
    deleteButton.addEventListener("click", deleteComment)
}

async function deleteComment(event) {
    let comment = event.target.parentElement.parentElement;
    loader(comment);
    comment.classList.add("disable-form");
    let formData = new FormData();
    let csrf = form.elements.namedItem("_csrf").value;
    formData.append("_csrf", csrf);
    formData.append("id", event.target.value);
    let response = await fetch("/deleteComment", {
        method: "post",
        body: formData
    });
    if (response.status === 200) {
        let body = await response.json();
        if (body === 1) {
            commentSection.removeChild(comment);
        } else {
            alert("cannot delete message, try again later");
        }
    } else if (response.status === 400) {
        alert("bad request, try another time");
    }
}

function updateComment(event) {
    let comment = event.target.parentElement.parentElement;
    let contentContainer = comment.children[2];
    contentContainer.contentEditable = true;
    contentContainer.focus();
    let oldContent = contentContainer.innerHTML;
    let commentButtonsContainer = comment.children[3];
    let id = commentButtonsContainer.lastElementChild.value;
    commentButtonsContainer.style.display = "none";
    let updateButtonsContainer = document.createElement("div");
    updateButtonsContainer.classList.add("comment-button");
    let cancelButton = document.createElement("button");
    cancelButton.type = "button";
    cancelButton.innerHTML = "Cancel";
    updateButtonsContainer.appendChild(cancelButton);
    comment.append(updateButtonsContainer);
    cancelButton.addEventListener("click", () => {
        comment.removeChild(updateButtonsContainer);
        commentButtonsContainer.style.display = "block";
        contentContainer.innerHTML = oldContent;
        contentContainer.contentEditable = false;
    });
    let updateButton = document.createElement("button");
    updateButton.type = "button";
    updateButton.innerHTML = "Update";
    updateButtonsContainer.appendChild(updateButton);
    updateButton.addEventListener("click", async () => {
        let loaderElement = loader(comment);
        comment.classList.add("disable-form");
        let formData = new FormData();
        let csrf = form.elements.namedItem("_csrf").value;
        formData.append("_csrf", csrf);
        formData.append("id", id);
        formData.append("message", contentContainer.innerHTML);
        print(contentContainer.innerHTML)
        let response = await fetch("/updateComment", {
            method: "post",
            body: formData
        });
        if (response.status === 200) {
            let body = await response.text();
            if (body === "1") {
                comment.removeChild(loaderElement);
                comment.removeChild(updateButtonsContainer);
                comment.classList.remove("disable-form");
                commentButtonsContainer.style.display = "block";
                contentContainer.contentEditable = false;
            } else {
                alert("cannot update message, try again later");
            }

        } else if (response.status === 400) {
            alert("bad request, try another time");
        }
    })
}

//loading animation
function loader(parentElement) {
    let loader = document.createElement("div");
    loader.classList.add("loader");
    parentElement.append(loader);
    return loader;
}

//swiper library
let swiper = new Swiper('.swiper-container', {
    navigation: {
        nextEl: '.swiper-button-next',
        prevEl: '.swiper-button-prev',
    },
});

//sign in form
let formSignIn = document.getElementById("form-signin");
if (formSignIn != null) {
    //display signin form
    let displayLogInFormButtons = document.getElementsByClassName("display-login-form-button");
    for (let button of displayLogInFormButtons) {
        button.addEventListener("click", (event) => {
            formSignIn.classList.remove("not-required");
            formSignIn.classList.add("form-signin");
        });
    }
    //hide signin form
    let quitButton = document.getElementById("quit-button");
    quitButton.addEventListener("click", () => {
        formSignIn.classList.remove("form-signin");
        formSignIn.classList.add("not-required");
    });
    //check user input
    formSignIn.addEventListener("submit", event => {
        event.preventDefault();
        let formData = new FormData(formSignIn);
        fetch("/login", {
            method: "post",
            body: formData
        }).then(response => {
            if (response.url.includes("error")) {
                alert("wrong user or password");
            } else {
                location.reload();
            }
        })
    });
}

//add to cart
let cart = document.getElementById("add-to-cart");
if (cart != null) {
    cart.addEventListener("submit", async event => {
        event.preventDefault();
        let cartForm = new FormData(cart);
        let response = await fetch("/addToCart", {
            method: "post",
            body: cartForm
        });
        if (response.status === 400) {
            let body = await response.text();
            alert(body);
        } else {
            let numberOfItems = document.getElementById("number-of-items");
            numberOfItems.innerHTML = (parseInt(numberOfItems.innerHTML) + parseInt(cart.elements.namedItem("quantity").value)).toString();
        }

    });
}
//navigation dropdown bar
navigationBarIcon();

//auto height adjustment for comment box when comment typed overflow
let message = document.getElementById("message");
if (message != null) {
    message.setAttribute("style", "height:" + (message.scrollHeight) + "px;overflow-y:hidden;");
    message.addEventListener("input", onInput);
}

function onInput() {
    this.style.height = 'auto';
    this.style.height = (this.scrollHeight) + 'px';
}

function print(stuff) {
    console.log(stuff);
}

console.log("finished");


