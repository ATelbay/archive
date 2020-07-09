class ItemsController < ApplicationController

  before_action :find_item, only: [:show, :edit, :update, :destroy]
  # before_action :check_if_admin, only: [:edit, :update, :new, :create, :destroy]

  def index
    @items = Item.all
  end

  def create
    p "1---------->#{params[:item]}"
    @item = Item.create(item_params)
    if @item.errors.empty?
      redirect_to item_path(@item)
    else
      render "new"
    end
    # if @item.save
    #   redirect_to items_path notice: "Your Post was saved"
    # else
    #   render "new"
    # end
  end

  def edit

  end

  def show
    # unless @item = Item.where(id: params[:id]).first
    # render text: "Page not found", status: 404
    p "=================#{params[:id]}"
    @item = Item.where(id: params[:id]).first
    # end
  end

  def update
    p "1---------->#{params[:item]}"

    @item.update_attributes(item_params)
    if @item.errors.empty?
      redirect_to item_path(@item)
    else
      render "edit"
    end
  end

  def destroy

    @item.destroy
    redirect_to action: "index"
  end

  def upvote

  end

  def new
    @item = Item.new
  end

  private

  def find_item
    @item = Item.where(params[:id]).first
    render_404 unless @item
  end

  def check_if_admin
    render text: " Access denied", status: 403 unless params[:admin]
  end

  def item_params
    params.require(:item).permit!
  end

end
