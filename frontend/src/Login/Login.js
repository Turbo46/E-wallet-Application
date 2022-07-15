import React, {useEffect, useState} from "react";
import{ Link, useNavigate } from "react-router-dom"
import '../Login/Login.css';

function Login(){
    const [usermail, SetUsermail] = useState("");
    const [submit, setSubmit] = useState(false);

    const validateEmail = /^[a-zA-Z0-9]+([a-zA-Z0-9](_|-| )[a-zA-Z0-9])*[a-zA-Z0-9]+$/;

    let navigate = useNavigate();

    const userLogin = (() => {
        fetch(`http://localhost:8080/signin`, {
            method: "POST",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                "email": usermail
            })
        })
        .then((response) => {
            if(response.status === 202){
                navigate('/dashboard');
                // email became the key, and the value is the second parameters
                // sessionstorage & item have 2 parameters
                sessionStorage.setItem("email", usermail);
            }
        })
        .catch(error => console.log("error", error))
    })

    // Local storage -> long term storage
    // Session storage -> store data, but as soon close browser it will be clear -> login & logout will be clear

    const handleUserName = (event) => {
        SetUsermail(event.target.value);
    }

    const handleSubmitBtn = (event) => {
        event.preventDefault();
        setSubmit(!submit);
        console.log(submit);
        console.log("Email :", usermail);
        if (usermail && !validateEmail.test(usermail) === false) {
            userLogin();
            alert(`Login success! welcome ${usermail} !`);
        } else {
            alert(`Failed login!`)
            setSubmit(true);
        } 
    }

    useEffect(() => {
        console.log(submit);
        if (submit) {
            // alert("login button submit");
            // userLogin();
        }
    }, [submit])

    return(
        <div className="container">
            <form action="" className="form" onSubmit={handleSubmitBtn}>
                <h2 className="tit">Sign in to access your wallet</h2>
                <div className="insert">
                    <label htmlFor="" >User Email</label>
                    <input type="email" placeholder="example@mail.com" className="box" onChange={handleUserName} required/>
                    <div className="theButton">
                        <button onClick={userLogin} className="login">Sign In</button>
                    </div>
                    <Link to="/registration" className="yourText">
                        <a href = "#"
                        className = "yourText" > Didn 't have account? Create account here </a>
                    </Link>
                </div>
            </form>
        </div>
    )
}

export default Login;