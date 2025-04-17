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

import {Component, OnInit} from '@angular/core';
import {User} from '../user';
import {UserService} from '../user.service';
import {ActivatedRoute, Router} from '@angular/router';
import {FormBuilder, FormGroup, FormControl, Validators} from '@angular/forms';

@Component({
  selector: 'app-user-edit',
  templateUrl: './user-edit.component.html',
  styleUrls: ['./user-edit.component.css']
})
export class UserEditComponent implements OnInit {
  userEditForm: FormGroup;
  usernameCtrl: FormControl;
  passwordCtrl: FormControl;
  user: User;
  errorMessage: string;

  constructor(private formBuilder: FormBuilder, private userService: UserService, private route: ActivatedRoute, private router: Router) {
    this.user = {} as User;
    this.buildForm();
  }

  buildForm() {
    this.usernameCtrl = new FormControl('', [Validators.required, Validators.minLength(2)]);
    this.passwordCtrl = new FormControl('', []);
    this.userEditForm = this.formBuilder.group({
      username: this.usernameCtrl,
      password: this.passwordCtrl,
    });
  }

  initFormValues() {
    this.usernameCtrl.setValue(this.user.username);
    this.passwordCtrl.setValue(this.user.password);
  }

  ngOnInit() {
    // we use SpecResolver and UserResolver (get data before init component)
    this.user = this.route.snapshot.data.user;
    this.initFormValues();
  }

  onSubmit(user: User) {
    this.userService.updateUser(user.username.toString(), user).subscribe(
      res => {
        console.log('update success');
        this.gotoUserList();
      },
      error => this.errorMessage = error as any);

  }

  gotoUserList() {
    this.router.navigate(['/users']);
  }

}
