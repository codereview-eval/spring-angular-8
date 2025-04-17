/*
 *
 *  * Copyright 2016-2017 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

/**
 * @author Vitaliy Fedoriv
 */

import {RouterModule, Routes} from '@angular/router';
import {UserListComponent} from './user-list/user-list.component';
import {UserAddComponent} from './user-add/user-add.component';
import {UserEditComponent} from './user-edit/user-edit.component';
import {NgModule} from '@angular/core';
import {UserResolver} from './user-resolver';
import {SpecResolver} from '../specialties/spec-resolver';

const userRoutes: Routes = [
  {path: 'users', component: UserListComponent},
  {path: 'users/add', component: UserAddComponent},
  {path: 'users/:id/edit', component: UserEditComponent, resolve: {user: UserResolver, specs: SpecResolver}}
];

@NgModule({
  imports: [RouterModule.forChild(userRoutes)],
  exports: [RouterModule]
})

export class UsersRoutingModule {
}
