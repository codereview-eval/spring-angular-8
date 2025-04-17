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

import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {Observable} from 'rxjs';
import {User} from './user';
import {HttpClient} from '@angular/common/http';
import {HandleError, HttpErrorHandler} from '../error.service';
import {catchError} from 'rxjs/operators';


@Injectable()
export class UserService {

  entityUrl = environment.REST_API_URL + 'users';

  private readonly handlerError: HandleError;

  constructor(private http: HttpClient, private httpErrorHandler: HttpErrorHandler) {
    this.handlerError = httpErrorHandler.createHandleError('UserService');
  }

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.entityUrl)
      .pipe(
        catchError(this.handlerError('getUsers', []))
      );
  }

  getUserById(userId: string): Observable<User> {
    return this.http.get<User>((this.entityUrl + '/' + userId))
      .pipe(
        catchError(this.handlerError('getUserById', {} as User))
      );
  }

  updateUser(userId: string, user: User): Observable<User> {
    return this.http.put<User>(this.entityUrl + '/' + userId, user)
      .pipe(
        catchError(this.handlerError('updateUser', user))
      );
  }

  addUser(user: User): Observable<User> {
    return this.http.post<User>(this.entityUrl, user)
      .pipe(
        catchError(this.handlerError('addUser', user))
      );
  }

  deleteUser(userId: string): Observable<number> {
    return this.http.delete<number>(this.entityUrl + '/' + userId)
      .pipe(
        catchError(this.handlerError('deleteUser', 0))
      );
  }

}
