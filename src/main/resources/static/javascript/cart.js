navigationBarIcon();
let nothingYet = document.getElementById("nothingYet");
if (nothingYet == null) {
    let forms = document.getElementsByClassName("change-quantity");
    for (let form of forms) {
        form.addEventListener("submit", async event => {
            event.preventDefault();
            let list = event.target.parentElement;
            console.log(list);
            let loadingAnimation = loader(list);
            let formData = new FormData(form);
            let response = await fetch("/changeQuantity", {
                method: "post",
                body: formData
            });
            if (response.status === 200) {
                let body = await response.json();
                console.log(body);
                let numberOfItems = document.getElementById("number-of-items");
                let totalAmount = document.getElementById("totalAmount");
                numberOfItems.innerHTML = body.quantity;
                totalAmount.innerHTML = "total amount: " + body.totalAmount;
                list.removeChild(loadingAnimation);
            } else {
                list.removeChild(loadingAnimation);
                alert("bad request");
            }
        })
    }
    let stripe = Stripe('pk_test_51H3uwHBgep74jSnI3C44BmBUWZ4J75UnFrvDxffCx7mdifDI0RLfAmmE2VaQWPFWR6fxR6hk0TtL0BG6HfSPsvR800tVmQMAQ8');
    let elements = stripe.elements();
    // Custom styling can be passed to options when creating an Element.
    let style = {
        base: {
            // Add your base input styles here. For example:
            fontSize: '16px',
            color: '#32325d',
        },
    };

    // Create an instance of the card Element.
    let card = elements.create('card', {style: style});

    // Add an instance of the card Element into the `card-element` <div>.
    card.mount('#card-element');
    let form = document.getElementById('payment-form');
    form.addEventListener('submit', function (event) {
        event.preventDefault();
        stripe.createToken(card).then(function (result) {
            if (result.error) {
                // Inform the customer that there was an error.
                let errorElement = document.getElementById('card-errors');
                errorElement.textContent = result.error.message;
            } else {
                // Send the token to your server.
                stripeTokenHandler(result.token);
            }
        });
    });

    function stripeTokenHandler(token) {
        // Insert the token ID into the form so it gets submitted to the server
        let form = document.getElementById('payment-form');
        let hiddenInput = document.createElement('input');
        hiddenInput.setAttribute('type', 'hidden');
        hiddenInput.setAttribute('name', 'stripeToken');
        hiddenInput.setAttribute('value', token.id);
        form.appendChild(hiddenInput);

        // Submit the form
        form.submit();
    }
}
