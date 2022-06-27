import React from 'react';
import { CgProfile } from "react-icons/cg";
import {SiShopify} from "react-icons/si";
import {BsShopWindow, BsCardList} from "react-icons/bs";
import {MdOutlineAddBusiness} from "react-icons/md";
import {RiFileUserLine} from "react-icons/ri";
import {VscHistory} from "react-icons/vsc";
import {FaUserTie, FaUserAltSlash, FaStoreSlash} from "react-icons/fa";
import {AiOutlineApi} from "react-icons/ai";
import {IoIosStats} from "react-icons/io"

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
        title: 'Add External Service',
        path: '/add-external-service',
        icon: <AiOutlineApi />,
        cName: 'nav-text',
        adminOnly: true,
    },
    {
        title: 'Get User Information',
        path: '/view-subscriber-info-admin',
        icon: <RiFileUserLine />,
        cName: 'nav-text',
        adminOnly: true,
    },
    {
        title: 'User History',
        path: '/user-purchase-history',
        icon: <VscHistory />,
        cName: 'nav-text',
        adminOnly: true,
    },
    {
        title: 'Store History',
        path: '/select-store-history',
        icon: <BsCardList/>,
        cName: 'nav-text',
        adminOnly: true,
    },
    {
        title: 'Unsubscribe User',
        path: '/remove-user-subscription-admin',
        icon: <FaUserAltSlash />,
        cName: 'nav-text',
        adminOnly: true,
    },
    {
        title: 'Close Store Permanently',
        path: '/close-store-permanently-admin',
        icon: <FaStoreSlash />,
        cName: 'nav-text',
        adminOnly: true,
    },
    {
        title: 'Add Admin',
        path: '/add-admin',
        icon: <FaUserTie />,
        cName: 'nav-text',
        adminOnly: true,
    },
    {
        title: 'Stats',
        path: '/adminStats',
        icon: <IoIosStats />,
        cName: 'nav-text',
        adminOnly: true,
    }
];