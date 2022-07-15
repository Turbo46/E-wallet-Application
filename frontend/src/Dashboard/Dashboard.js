import React, {useEffect, useState} from "react";
import '../Dashboard/Dashboard.css'

function Dashboard() {
    const [topUpBalance, SetTopUpBalance] = useState();
    const [checkBalance, setCheckBalance] = useState();

    const [transferEmail, setTransferEmail] = useState("");
    const [transferAmount, setTransferAmount] = useState();

    const [hide, setHide] = useState(false);

    const [submit, setSubmit] = useState(false);

    const userCheckBalance = (() =>{
        fetch(`http://localhost:8080/check-balances/${sessionStorage.getItem("email")}`)
            .then((response) => response.json())
            .then((response) => {
                console.log("response: " , response)
                setCheckBalance(response)
            })
    })

    const userTopUp = (() => {
        fetch(`http://localhost:8080/top-up`, {
                method: "PUT",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    "email": sessionStorage.getItem("email"),
                    "balance": topUpBalance
                })
            })
            .catch(error => console.log("error", error))
    })

    const userTransfer = (()=>{
        fetch(`http://localhost:8080/transfer`, {
                method: "PUT",
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    "emailSource": sessionStorage.getItem("email"),
                    "emailTarget": transferEmail,
                    "balanceSource": transferAmount
                })
            })
            .catch(error => console.log("error", error))
    })

    const handleTopUpBalance = (event) => {
        SetTopUpBalance(event.target.value)
    }

    const handleTransferUserEmail = (event) => {
        setTransferEmail(event.target.value)
    }

    const handleTransferAmount = (event) => {
        setTransferAmount(event.target.value)
    }

    const handleSubmitBtnTopUp = (event) => {
        event.preventDefault();
        setSubmit(!submit);
        console.log("Top Up :", topUpBalance, "State :", submit);
        if ((topUpBalance <= 0)) {
            alert('Top up failed!');
        }else{
            userTopUp();
            alert(`Your top up ${topUpBalance} is success!`);
        }
    }

    const handleSubmitBtnTransfer = (event) => {
        event.preventDefault();
        setSubmit(!submit);
        console.log("Source email: ", sessionStorage.getItem("email"), "State :", submit);
        if((transferAmount <= 0)){
            alert("Please input your amout!");
        } else if (transferAmount > checkBalance) {
            alert('Your balance is not enough!');
        }else{
            userTransfer();
            alert(`Cool ${sessionStorage.getItem("email")}! Your: ${transferAmount} transfer to ${transferEmail} is succesed!`);
        }
    }

    const handleHide = (event) => {
        setHide(
            current => !current
        )
    }
    
    useEffect(() => {
        userCheckBalance();
    }, [submit])

    return(
        <div>
            <div className="information">
                <div className="kiri">
                    <label htmlFor="">Hello: {sessionStorage.getItem("email")}</label>
                </div>
                <div className="kanan">
                    <label htmlFor="">Your balance is : </label>
                    {
                        hide && (
                            <label htmlFor=""> IDR.{checkBalance}</label>
                        )
                    }
                </div>
                <div className="show-hide">
                    <button onClick={handleHide} className="hide">Show balance</button>
                </div> 
            </div>

            <div className="Mycontainer-2">
                <form action="" className="form1" onSubmit={handleSubmitBtnTopUp}>
                    <div className="theTitle">
                        <h2>Top up your wallet</h2>
                        <label htmlFor="">Amount</label>
                        <input className="form-box" type="number" placeholder="IDR. xxx.xxx.xxx" onChange={handleTopUpBalance} required/>
                        <button className="top">Top Up</button>
                    </div>
                </form>
            </div>
            
            <div className = "Mycontainer">
                <form action="" className="form2" onSubmit={handleSubmitBtnTransfer} >
                    <h2>Transfer money</h2>
                    <label htmlFor="">Receipt email</label>
                    <input className="form-box" type="email" placeholder="example@mail.com" onChange={handleTransferUserEmail} required/>
                    <label htmlFor="">Amount</label>
                    <input className="form-box" type="number" placeholder="IDR. xxx.xxx.xxx" onChange={handleTransferAmount} required />
                    <button className="trf">Transfer</button>
                </form>
            </div>
            <script src='https://kit.fontawesome.com/a076d05399.js'></script>
        </div>
    )
}

export default Dashboard;