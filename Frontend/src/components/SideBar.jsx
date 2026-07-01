import React from 'react';
import { Link } from 'react-router-dom';

const NavItems = [
  { to: "/", label: "Dashboard", icon: "fa-solid fa-house" },
  { to: "/purchase", label: "Purchase Grain", icon: "fa-solid fa-cart-shopping" },
  { to: "/farmer", label: "Farmer", icon: "fa-solid fa-tractor" }
];

const SideBar = () => {
  return (
    <aside className="w-40 h-screen bg-green-500 p-4">
      <nav className="h-full flex flex-col justify-start items-start gap-4">
        <ul className="flex flex-col items-start gap-4 text-white">
          {NavItems.map((item) => (
            <li key={item.to}>
              <Link to={item.to} className="flex items-center gap-2">
                <i className={item.icon}></i>
                <span>{item.label}</span>
              </Link>
            </li>
          ))}
        </ul>
      </nav>
    </aside>
  );
};

export default SideBar;