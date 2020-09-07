//loading animation
function loader(parentElement) {
    let loader = document.createElement("div");
    loader.classList.add("loader");
    parentElement.append(loader);
    return loader;
}