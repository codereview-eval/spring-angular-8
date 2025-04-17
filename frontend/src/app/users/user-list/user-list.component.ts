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

import {Component, OnInit} from '@angular/core';
import {User} from '../user';
import {UserService} from '../user.service';
import {Router} from '@angular/router';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {
  users: User[];
  errorMessage: string;
  responseStatus: number;
  isUserDataReceived: boolean = false;

  constructor(private userService: UserService, private router: Router) {
    this.users = [];
  }

  ngOnInit() {
    this.userService.getUsers().pipe(
      finalize(() => {
        this.isUserDataReceived = true;
      })
    ).subscribe(
      users => this.users = users,
      error => this.errorMessage = error as any);
  }

  deleteUser(user: User) {
    this.userService.deleteUser(user.username.toString()).subscribe(
      response => {
        this.responseStatus = response;
        this.users = this.users.filter(currentItem => !(currentItem.username == user.username));
      },
      error => this.errorMessage = error as any);
  }

  gotoHome() {
    this.router.navigate(['/welcome']);
  }

  addUser() {
    this.router.navigate(['/users/add']);
  }

  editUser(user: User) {
    this.router.navigate(['/users', user.username, 'edit']);
  }
}
