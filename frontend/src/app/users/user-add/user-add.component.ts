/*
 *
 *  * Copyright 2016-2018 the original author or authors.
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

import {Component} from '@angular/core';
import {User} from '../user';
import {Router} from '@angular/router';
import {UserService} from '../user.service';

@Component({
  selector: 'app-user-add',
  templateUrl: './user-add.component.html',
  styleUrls: ['./user-add.component.css']
})
export class UserAddComponent {
  user: User;
  errorMessage: string;

  constructor(private userService: UserService, private router: Router) {
    this.user = {} as User;
  }

  onSubmit(user: User) {
    this.userService.addUser(user).subscribe(
      newUser => {
        this.user = newUser;
        this.gotoUserList();
      },
      error => this.errorMessage = error as any
    );
  }

  gotoUserList() {
    this.router.navigate(['/users']);
  }
}
