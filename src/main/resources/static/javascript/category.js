navigationBarIcon();
const queryString = window.location.search;
const urlSearchParams = new URLSearchParams(queryString);
const currentPage = urlSearchParams.get("page");
console.log(currentPage);
let pages = document.getElementById("pagination").children;
for (let page of pages) {
    if (page.innerHTML === currentPage) {
        page.classList.add("current");
    }
}
