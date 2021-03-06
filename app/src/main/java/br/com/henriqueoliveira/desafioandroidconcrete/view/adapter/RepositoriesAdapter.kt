package br.com.henriqueoliveira.desafioandroidconcrete.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.henriqueoliveira.desafioandroidconcrete.R
import br.com.henriqueoliveira.desafioandroidconcrete.helpers.loadUrl
import br.com.henriqueoliveira.desafioandroidconcrete.service.model.Repository
import kotlinx.android.synthetic.main.repository_item.view.*

typealias ItemClickListener = (Repository) -> Unit

class RepositoriesAdapter(private var repositoryItems: List<Repository>, private val listener: ItemClickListener) : RecyclerView.Adapter<RepositoriesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.repository_item, parent, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.populateItem(repositoryItems[position])
    }

    override fun getItemCount(): Int {
        return repositoryItems.size
    }

    fun updateList(list: List<Repository>, normalUpdate: Boolean = true) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(old: Int, new: Int) = repositoryItems[old].repoName == list[new].repoName && repositoryItems[old].owner == list[new].owner
            override fun getOldListSize() = repositoryItems.size
            override fun getNewListSize() = list.size
            override fun areContentsTheSame(old: Int, new: Int) = repositoryItems[old].repoName == list[new].repoName && repositoryItems[old].owner == list[new].owner &&
                    normalUpdate
        })
        repositoryItems = list.toMutableList()
        diff.dispatchUpdatesTo(this)
    }

    class ViewHolder(itemView: View, private val listener: ItemClickListener) : RecyclerView.ViewHolder(itemView) {

        fun populateItem(item: Repository) {
            item.let { _ ->
                itemView.repositoryName.text = item.repoName
                itemView.fullName.text = item.fullName
                itemView.repositoryDescription.text = item.description
                itemView.user.text = item.owner.login
                itemView.forks.text = item.forksCount.toString()
                itemView.starsCount.text = item.starsCount.toString()
                itemView.acount.loadUrl(item.owner.avatarUrl)
                itemView.setOnClickListener { listener.invoke(item) }
            }
        }
    }
}
