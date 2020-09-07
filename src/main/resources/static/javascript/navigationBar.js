let navigationBarIcon = () => {
    let barIcon = document.getElementById("bar-icon");
    let navigationBar = document.getElementById("navigation-bar");
    barIcon.addEventListener("click", () => {
        let hasClassClicked = navigationBar.classList.contains("clicked");
        if (hasClassClicked) {
            navigationBar.classList.remove("clicked");
        } else {
            navigationBar.classList.add("clicked");
        }
    });
};