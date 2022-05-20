import React from 'react';
import { CgProfile } from "react-icons/cg";
import {SiShopify} from "react-icons/si";
import {BsShopWindow, BsCardList} from "react-icons/bs";
import {MdOutlineAddBusiness} from "react-icons/md";
import {VscHistory} from "react-icons/vsc";
import {FaUserAltSlash, FaStoreSlash} from "react-icons/fa";

export const SidebarData = [
    {
        title: 'Profile',
        path: '/user-profile-subscriber',
        icon: <CgProfile />,
        cName: 'nav-text'
    },
    {
        title: 'Purchase History',
        path: '/personal-purchase-history',
        icon: <SiShopify />,
        cName: 'nav-text'
    },
    {
        title: 'Manage My Stores',
        path: '/manage-stores',
        icon: <BsShopWindow />,
        cName: 'nav-text'
    },
    {
        title: 'Open New Store',
        path: '/open-new-store',
        icon: <MdOutlineAddBusiness />,
        cName: 'nav-text'
    },
    {
        title: 'User History',
        path: '/user-purchase-history',
        icon: <VscHistory />,
        cName: 'nav-text'
    },
    {
        title: 'Store History',
        path: '/store-purchase-history',
        icon: <BsCardList/>,
        cName: 'nav-text'
    },
    {
        title: 'Unsubscribe User',
        path: '/remove-user-subscription-admin',
        icon: <FaUserAltSlash />,
        cName: 'nav-text'
    },
    {
        title: 'Close Store Permanently',
        path: '/close-store-permanently-admin',
        icon: <FaStoreSlash />,
        cName: 'nav-text'
    }
];