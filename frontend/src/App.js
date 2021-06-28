import * as React from "react";
import {Admin, Resource} from 'react-admin';
import {DataProvider, resources} from "./PaymentDataProvider";
import {ClientList} from "./ClientList";
import {CreateClient} from "./CreateClient";
import {AccountList} from "./AccountList";
import {PaymentList} from "./PaymentList";
import {CreatePayment} from "./CreatePayment";
import {PaymentInfoList} from './PaymentInfoList';
import PaymentIcon from '@material-ui/icons/Payment';
import PeopleIcon from '@material-ui/icons/People';
import ReceiptIcon from '@material-ui/icons/Receipt';
import AccountBalanceWalletIcon from '@material-ui/icons/AccountBalanceWallet';

function App() {
    return (
        <Admin dataProvider={new DataProvider()}>
            <Resource name={resources.clients} list={ClientList} create={CreateClient} icon={PeopleIcon}/>
            <Resource name={resources.accounts} list={AccountList} icon={AccountBalanceWalletIcon}/>
            <Resource name={resources.payments} list={PaymentList} create={CreatePayment} icon={PaymentIcon}/>
            <Resource name={resources.paymentInfo} list={PaymentInfoList} icon={ReceiptIcon} options={{label: "Payment Info"}}/>
        </Admin>
    );
}

export default App;
