import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';

import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown icon="th-list" name="Entities" id="entity-menu" data-cy="entity" style={{ maxHeight: '80vh', overflow: 'auto' }}>
    <MenuItem icon="asterisk" to="/book">
      Book
    </MenuItem>
    <MenuItem icon="asterisk" to="/book-item">
      Book Item
    </MenuItem>
    <MenuItem icon="asterisk" to="/reader">
      Reader
    </MenuItem>
    <MenuItem icon="asterisk" to="/rack">
      Rack
    </MenuItem>
    <MenuItem icon="asterisk" to="/author">
      Author
    </MenuItem>
    <MenuItem icon="asterisk" to="/catagory">
      Catagory
    </MenuItem>
    <MenuItem icon="asterisk" to="/publisher">
      Publisher
    </MenuItem>
    <MenuItem icon="asterisk" to="/book-reservation">
      Book Reservation
    </MenuItem>
    <MenuItem icon="asterisk" to="/book-lending">
      Book Lending
    </MenuItem>
    <MenuItem icon="asterisk" to="/book-lending-details">
      Book Lending Details
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
