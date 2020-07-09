class User < ApplicationRecord
  # attr_accessor :login

  devise :databse_authenticatable, :registrable, :recoverable, :rememberable, :validatable

  attr_accessor :login, :email, :password, :password_cofirmation, :remember_me
  has_one :cart
  has_many :orders
end
