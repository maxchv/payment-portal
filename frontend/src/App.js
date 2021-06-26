import "./main.css";
import Clients from "./Clients";
import Payments from "./Payments";
import {BrowserRouter as Router, Redirect, Route, Switch} from "react-router-dom";

import {Jumbotron, Nav, Navbar, NavItem} from "react-bootstrap";

const Main = () => {
    return <>
        <Jumbotron>
            <h1>Payment portal</h1>
        </Jumbotron>
    </>
}

const SwaggerUi = (props) => {
    return <Redirect to={props.location}/>;
}

const NotFound = () => {
    return <>
        <h2>Not found</h2>
    </>;
}

function App() {
    return (
        <Router>
            <div className="row">
                <Navbar bg="light" expand="lg">
                    <Navbar.Header>
                        <Navbar.Brand>
                            <a href="/">Payment portal</a>
                        </Navbar.Brand>
                        <Navbar.Toggle/>
                    </Navbar.Header>
                    <Navbar.Collapse>
                        <Nav>
                            <NavItem href="/clients">Clients</NavItem>
                            <NavItem href="/payments">Payments</NavItem>
                        </Nav>
                        <Nav pullRight>
                            <NavItem href="/api">API</NavItem>
                        </Nav>
                    </Navbar.Collapse>
                </Navbar>
            </div>
            <div className="row">
                <Switch>
                    <Route exact path="/">
                        <Main/>
                    </Route>
                    <Route path="/clients">
                        <>
                            <h2>Clients</h2>
                            <Clients/>
                        </>
                    </Route>
                    <Route path="/payments">
                        <>
                            <h2>Payments</h2>
                            <Payments/>
                        </>
                    </Route>
                    <Route path="/api">
                        <SwaggerUi location="/swagger-ui"/>
                    </Route>
                    <Route path="*">
                        <NotFound/>
                    </Route>
                </Switch>
            </div>
        </Router>
    );
}

export default App;
