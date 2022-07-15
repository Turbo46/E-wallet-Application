import React, {useState, useEffect} from "react";
import{ useNavigate } from "react-router-dom"
import '../Registration/Registration.css';

function Registration(){
    const [usermail, SetUsermail] = useState("");
    const [status, setStatus] = useState(false);

    // const [emailError, setEmailError] = useState(false);

    const validateEmail = /^[a-zA-Z0-9]+([a-zA-Z0-9](_|-| )[a-zA-Z0-9])*[a-zA-Z0-9]+$/;

    const navigate = useNavigate();

    const userRegister = (() => {
        fetch(`http://localhost:8080/signup`, {
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
                if (response.status === 202) {
                    navigate('/');
                }
                console.log(response);
            })
            .catch(error => console.log("error", error))
    })

    const handleRegister = (event) => {
        SetUsermail(event.target.value)
    }

    const handleSubmit = (event) => {
        event.preventDefault();
        setStatus(!status);
        console.log(status);
        console.log("Email :", usermail);

        if(usermail && !validateEmail.test(usermail) === true){
            userRegister();
            alert(`Register success! welcome ${usermail} !`);
        }else{
            alert(`Failed!`);
            // setEmailError(true);
            setStatus(true);
        }
    }

    useEffect(() => {
    }, [status])

    return(
        <div className="container">
            <form action="" className="form" onSubmit={handleSubmit}>
                <h2 className="tit">Create your own wallet</h2>
                <div className="insert2">
                    <label htmlFor="" >User Email</label>
                    <input type="email" placeholder="example@mail.com" className="box" onChange={handleRegister} required/>
                </div>
                <div className="theButton2">
                    <button onClick={userRegister} className="register">Create Account</button>
                </div>
            </form>
        </div>
    )
}

export default Registration;